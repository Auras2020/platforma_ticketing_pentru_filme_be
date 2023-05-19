package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.ProductDetailsDto;
import com.example.platforma_ticketing_be.dtos.ShowTimingDto;
import com.example.platforma_ticketing_be.entities.BookedProduct;
import com.example.platforma_ticketing_be.entities.Seat;
import com.example.platforma_ticketing_be.entities.ShowTiming;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.repository.BookedProductRepository;
import com.example.platforma_ticketing_be.repository.SeatRepository;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
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
    private final BookedProductRepository bookedProductRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public SeatService(SeatRepository seatRepository, BookedProductRepository bookedProductRepository, ModelMapper modelMapper, EmailServiceImpl emailService) {
        this.seatRepository = seatRepository;
        this.bookedProductRepository = bookedProductRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    private void displayParagraph(String message, Document document, Font fontUser, float indentation){
        Paragraph messageParagraph = new Paragraph(message, fontUser);
        messageParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        messageParagraph.setIndentationLeft(indentation);
        document.add(messageParagraph);
    }

    private void displayParagraphWithSpaceAfter(String message, Document document, Font fontUser, float indentation){
        Paragraph messageParagraph = new Paragraph(message, fontUser);
        messageParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        messageParagraph.setIndentationLeft(indentation);
        messageParagraph.setSpacingAfter(10);
        document.add(messageParagraph);
    }

    private float lastParagrapghYCoordinate(PdfWriter writer){
        PdfContentByte contentByte = writer.getDirectContent();
        return contentByte.getPdfDocument().getVerticalPosition(true);
    }

    private void displayOrangeLine(PdfWriter writer, Document document){
        PdfContentByte contentByte = writer.getDirectContent();
        contentByte.setColorStroke(Color.ORANGE);
        contentByte.setLineWidth(2);

        float y = lastParagrapghYCoordinate(writer);
        contentByte.moveTo(document.left(), y);
        contentByte.lineTo(document.right(), y);

        contentByte.stroke();
    }

    private void displayImage(PdfWriter writer, Document document) throws IOException {
        Image image = Image.getInstance("classpath:qr.png");
        float x = 600;
        float y = lastParagrapghYCoordinate(writer);
        image.scaleAbsolute(200, 200);
        image.setAbsolutePosition(x, y);
        document.add(image);
    }

    public InputStreamResource getSeatsPdfContent(ShowTimingDto showTimingDto, List<String> seats) throws DocumentException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A3);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 24, Font.BOLD);

        Paragraph title = new Paragraph("Tickets", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);

        document.add(title);

        PdfContentByte contentByte = writer.getDirectContent();
        contentByte.setColorStroke(Color.BLUE);
        contentByte.setLineWidth(2);
        contentByte.moveTo(document.left(), document.top() - 50);
        contentByte.lineTo(document.right(), document.top() - 50);
        contentByte.stroke();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setSize(16);

        for(int i = 0; i < seats.size(); i++){
            if(i % 4 == 0 && i != 0){
                document.newPage();
            }
            displayParagraph("Ticket " +  (i + 1), document, fontTitle, 0);
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
            displayParagraph("Venue: " + showTimingDto.getVenue().getVenueNumber(), document, font, 0);

            int i1 = 0;
            int j1 = 0;

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(seats.get(i));

                i1 = jsonNode.get("i1").asInt();
                j1 = jsonNode.get("j1").asInt();
            } catch (Exception e) {
                e.printStackTrace();
            }

            displayParagraph("Row: " + i1, document, font, 0);
            displayParagraph("Column: " + j1, document, font, 0);

            displayImage(writer, document);

            displayParagraph("\n", document, fontTitle, 0);
            displayOrangeLine(writer, document);
            displayParagraph("\n", document, fontTitle, 0);
        }

        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(14);
        displayParagraph("Total price of tickets: " + seats.size() * showTimingDto.getPrice() + " RON", document, font, 0);

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(in);

        out.close();
        in.close();

        return file;
    }

    public InputStreamResource getProductsPdfContent(ShowTimingDto showTimingDto, List<ProductDetailsDto> productDetails) throws DocumentException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A3);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 24, Font.BOLD);

        Paragraph title = new Paragraph("Products", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);

        document.add(title);

        PdfContentByte contentByte = writer.getDirectContent();
        contentByte.setColorStroke(Color.BLUE);
        contentByte.setLineWidth(2);
        contentByte.moveTo(document.left(), document.top() - 50);
        contentByte.lineTo(document.right(), document.top() - 50);
        contentByte.stroke();

        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontTitle.setSize(16);

        for(int i = 0; i < productDetails.size(); i++){
            if(i % 4 == 0 && i != 0){
                document.newPage();
            }
            displayParagraph("Product " +  (i + 1), document, fontTitle, 0);
            displayParagraphWithSpaceAfter("", document, fontTitle, 0);

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
            displayParagraph("Venue: " + showTimingDto.getVenue().getVenueNumber(), document, font, 0);

            displayParagraph("Name: " + productDetails.get(i).getName(), document, font, 0);
            displayParagraph("Price: " + productDetails.get(i).getPrice() + " RON", document, font, 0);
            displayParagraph("Quantity: " + productDetails.get(i).getQuantity() + "g", document, font, 0);
            displayParagraph("Number: " + productDetails.get(i).getNumber(), document, font, 0);

            displayImage(writer, document);

            displayParagraph("\n", document, fontTitle, 0);
            displayOrangeLine(writer, document);
        }

        int sum = 0;
        for(ProductDetailsDto productDetailsDto: productDetails){
            sum += productDetailsDto.getNumber();
        }

        int price = 0;
        for(ProductDetailsDto productDetailsDto: productDetails){
            price += productDetailsDto.getNumber() * productDetailsDto.getPrice();
        }

        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(14);
        displayParagraphWithSpaceAfter("", document, fontTitle, 0);
        displayParagraph("Total number of products: " + sum, document, font, 0);
        displayParagraph("Total price of products: " + price + " RON", document, font, 0);

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(in);

        out.close();
        in.close();

        return file;
    }

    public void create(ShowTimingDto showTimingDto, List<String> seats, List<ProductDetailsDto> productDetails, UserAccount user, String status) throws DocumentException, IOException{
        ShowTiming showTiming = this.modelMapper.map(showTimingDto, ShowTiming.class);
        for(String seat: seats){
            Seat seat1 = new Seat(showTiming, seat, user, status);
            seatRepository.save(seat1);
        }
        for(ProductDetailsDto productDetailsDto: productDetails){
            BookedProduct bookedProduct = new BookedProduct(showTiming, user, productDetailsDto.getName(), productDetailsDto.getQuantity(), productDetailsDto.getNumber(), status);
            bookedProductRepository.save(bookedProduct);
        }

        List<DataSource> dataSourceList = new ArrayList<>();

        InputStreamResource file = getSeatsPdfContent(showTimingDto, seats);
        InputStream inputStream = file.getInputStream();
        DataSource dataSource = new ByteArrayDataSource(inputStream, "application/pdf");
        dataSourceList.add(dataSource);

        if(productDetails.size() > 0) {
            InputStreamResource file1 = getProductsPdfContent(showTimingDto, productDetails);
            InputStream inputStream1 = file1.getInputStream();
            DataSource dataSource1 = new ByteArrayDataSource(inputStream1, "application/pdf");
            dataSourceList.add(dataSource1);
        }

        if(dataSourceList.size() == 1){
            this.emailService.processEmailWithAttachments("Thank you for choosing Hot Movies Center platform! " +
                            "Below you have attachments with booked tickets.",
                    "Hot Movies Center - Booked Tickets", user.getEmail(), dataSourceList);
        } else if(dataSourceList.size() == 2) {
            this.emailService.processEmailWithAttachments("Thank you for choosing Hot Movies Center platform! " +
                            "Below you have attachments with booked tickets and bought foods and/or drinks.",
                    "Hot Movies Center - Booked Tickets", user.getEmail(), dataSourceList);
        }
    }

    public Set<String> findSeatsByShowTimingId(Long id){
        return this.seatRepository.findSeatsByShowTimingId(id);
    }
}
