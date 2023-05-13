package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.ShowTimingDto;
import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.repository.SeatRepository;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public SeatService(SeatRepository seatRepository, ModelMapper modelMapper, EmailServiceImpl emailService) {
        this.seatRepository = seatRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    private void displayParagraph(String message, Document document, Font fontUser, float indentation){
        Paragraph messageParagraph = new Paragraph(message, fontUser);
        messageParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        messageParagraph.setIndentationLeft(indentation);
        document.add(messageParagraph);
    }

    private void displayParagraphWithBorderBottom(String message, Document document, Font fontUser, float indentation, PdfWriter writer){
        Paragraph messageParagraph = new Paragraph(message, fontUser);
        messageParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        messageParagraph.setIndentationLeft(indentation);

        messageParagraph.add("\n");

        PdfContentByte contentByte = writer.getDirectContent();
        contentByte.setColorStroke(Color.ORANGE);
        contentByte.setLineWidth(2);

        float y = document.top() - messageParagraph.getFont().getCalculatedLeading(messageParagraph.getFont().getSize()) - messageParagraph.getFont().getSize() - 20;
        contentByte.moveTo(document.left(), y);
        contentByte.lineTo(document.right(), y);

        // Stroke the line
        contentByte.stroke();

        document.add(messageParagraph);
    }

    public InputStreamResource export(ShowTimingDto showTimingDto, List<String> seats) throws DocumentException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A3);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        // Create a font for the title
        Font titleFont = new Font(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 24, Font.BOLD);

        // Create a paragraph with the title
        Paragraph title = new Paragraph("Tickets", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);

        // Add the title to the document
        document.add(title);

        PdfContentByte contentByte = writer.getDirectContent();

        // Set the stroke color to blue
        contentByte.setColorStroke(Color.BLUE);

        // Set the line width
        contentByte.setLineWidth(2);

        // Draw a horizontal line
        contentByte.moveTo(document.left(), document.top() - 50); // starting point of the line
        contentByte.lineTo(document.right(), document.top() - 50); // ending point of the line

        // Stroke the line
        contentByte.stroke();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setSize(16);

        for(int i = 0; i < seats.size(); i++){
            displayParagraph("Ticket " + 1, document, fontTitle, 0);
            displayParagraph("\n", document, fontTitle, 0);

            Font fontUser = FontFactory.getFont(FontFactory.TIMES_BOLD);
            fontUser.setSize(14);

            String pattern = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            displayParagraph("Movie name: " + showTimingDto.getMovie().getName(), document, fontUser, 0);
            displayParagraph("Day: " + sdf.format(showTimingDto.getDay()), document, fontUser ,0);
            displayParagraph("Time: " + daysOfWeek[showTimingDto.getDay().getDay()] + " " + showTimingDto.getTime(), document, fontUser ,0);

            Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
            font.setSize(14);

            displayParagraph("Theatre name: " + showTimingDto.getTheatre().getName(), document, font, 0);
            displayParagraphWithBorderBottom("Venue: " + showTimingDto.getVenue().getVenueNumber(), document, font, 0, writer);
        }

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(in);

        out.close();
        in.close();

        return file;
    }

    public void create(ShowTimingDto showTimingDto, List<String> seats) throws DocumentException, IOException{
        ShowTiming showTiming = this.modelMapper.map(showTimingDto, ShowTiming.class);
        for(String seat: seats){
            Seat seat1 = new Seat(showTiming, seat);
            seatRepository.save(seat1);
        }

        List<DataSource> dataSourceList = new ArrayList<>();
        //for(int i = 0; i < seats.size(); i++){
            InputStreamResource file = export(showTimingDto, seats);
            InputStream inputStream = file.getInputStream();
            DataSource dataSource = new ByteArrayDataSource(inputStream, "application/pdf");

            dataSourceList.add(dataSource);
        //}

        this.emailService.processEmailWithAttachments("hello", "tickets", "andreipop767@gmail.com", dataSourceList);
    }

    public Set<String> findSeatsByShowTimingId(Long id){
        return this.seatRepository.findSeatsByShowTimingId(id);
    }
}
