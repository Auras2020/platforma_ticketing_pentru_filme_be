package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.dtos.*;
import com.example.platforma_ticketing_be.entities.*;
import com.example.platforma_ticketing_be.repository.*;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderSpecificationImpl orderSpecification;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private final ProductRepository productRepository;
    private final PeoplePromotionRepository peoplePromotionRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public OrderService(OrderRepository orderRepository, OrderSpecificationImpl orderSpecification, ModelMapper modelMapper, EmailServiceImpl emailService, ProductRepository productRepository, PeoplePromotionRepository peoplePromotionRepository) {
        this.orderRepository = orderRepository;
        this.orderSpecification = orderSpecification;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.productRepository = productRepository;
        this.peoplePromotionRepository = peoplePromotionRepository;
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
        com.lowagie.text.Image image = com.lowagie.text.Image.getInstance("classpath:qr.png");
        float x = 600;
        float y = lastParagrapghYCoordinate(writer);
        image.scaleAbsolute(200, 200);
        image.setAbsolutePosition(x, y);
        document.add(image);
    }

    public InputStreamResource getSeatsPdfContent(ShowTimingDto showTimingDto, List<String> seats,
                                                  int nrAdults, int nrStudents, int nrChilds,
                                                  float ticketsPrice, int ticketsDiscount) throws DocumentException, IOException {
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

        PeoplePromotion peoplePromotion = this.peoplePromotionRepository.findPeoplePromotionByShowTimingId(showTimingDto.getId());

        for(int i = 0; i < seats.size(); i++){
            if(i % 3 == 0 && i != 0){
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
            if(i < nrAdults){
                displayParagraph("Category: Adult", document, font, 0);
                if(peoplePromotion != null){
                    displayParagraph("Price: " + peoplePromotion.getAdult(), document, font, 0);
                } else {
                    displayParagraph("Price: " + showTimingDto.getPrice(), document, font, 0);
                }
            }
            if(i >= nrAdults && i < nrAdults + nrStudents){
                displayParagraph("Category: Student", document, font, 0);
                if(peoplePromotion != null){
                    displayParagraph("Price: " + peoplePromotion.getStudent(), document, font, 0);
                } else {
                    displayParagraph("Price: " + showTimingDto.getPrice(), document, font, 0);
                }
            }
            if(i >= nrAdults + nrStudents && i < nrAdults + nrStudents + nrChilds){
                displayParagraph("Category: Child", document, font, 0);
                if(peoplePromotion != null){
                    displayParagraph("Price: " + peoplePromotion.getChild(), document, font, 0);
                } else {
                    displayParagraph("Price: " + showTimingDto.getPrice(), document, font, 0);
                }
            }
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
        if(ticketsDiscount != 0){
            displayParagraph("Total price of tickets: " + ticketsPrice + " RON(" + ticketsDiscount + "% discount)", document, font, 0);
        } else {
            displayParagraph("Total price of tickets: " + ticketsPrice + " RON", document, font, 0);
        }

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(in);

        out.close();
        in.close();

        return file;
    }

    public InputStreamResource getProductsPdfContent(ShowTimingDto showTimingDto, List<ProductDetailsDto> productDetails,
                                                     float productsPrice, int productsDiscount) throws DocumentException, IOException {
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

        Font font = FontFactory.getFont(FontFactory.TIMES_BOLD);
        font.setSize(14);
        displayParagraphWithSpaceAfter("", document, fontTitle, 0);
        displayParagraph("Total number of products: " + sum, document, font, 0);
        if(productsDiscount != 0){
            displayParagraph("Total price of products: " + productsPrice + " RON(" + productsDiscount + "% discount)", document, font, 0);
        } else {
            displayParagraph("Total price of products: " + productsPrice + " RON", document, font, 0);
        }

        document.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(in);

        out.close();
        in.close();

        return file;
    }

    private Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public void createOrder(ShowTimingDto showTimingDto, List<String> seats, List<ProductDetailsDto> productDetails,
                            UserAccount user, String ticketStatus, String productStatus,
                            int nrAdults, int nrStudents, int nrChilds,
                            float ticketsPrice, float productsPrice,
                            int ticketsDiscount, int productsDiscount) throws DocumentException, IOException{
        ShowTiming showTiming = this.modelMapper.map(showTimingDto, ShowTiming.class);
        if(productDetails.size() == 0){
            int j = 0;
            for (String seat : seats) {
                Orders order;
                if(j == 0){
                    order = new Orders(showTiming, user, seat, ticketStatus, null, 0, null, ticketsPrice, productsPrice, getCurrentDate());
                } else {
                    order = new Orders(showTiming, user, seat, ticketStatus, null, 0, null, 0, 0, getCurrentDate());
                }
                this.orderRepository.save(order);
                j++;
            }
        } else if(seats.size() >= productDetails.size()){
            for(int i = 0; i < productDetails.size(); i++){
                Optional<Product> product = this.productRepository.findById(productDetails.get(i).getId());
                Orders order;
                if(i == 0){
                    order = new Orders(showTiming, user, seats.get(i), ticketStatus, product.get(), productDetails.get(i).getNumber(), productStatus, ticketsPrice, productsPrice, getCurrentDate());
                } else {
                    order = new Orders(showTiming, user, seats.get(i), ticketStatus, product.get(), productDetails.get(i).getNumber(), productStatus, 0, 0, getCurrentDate());
                }
                this.orderRepository.save(order);
            }
            for(int i = productDetails.size(); i < seats.size(); i++){
                Orders order = new Orders(showTiming, user, seats.get(i), ticketStatus, null, 0, productStatus, 0, 0, getCurrentDate());
                this.orderRepository.save(order);
            }
        } else {
            for(int i = 0; i < seats.size(); i++){
                Optional<Product> product = this.productRepository.findById(productDetails.get(i).getId());
                Orders order;
                if(i == 0){
                    order = new Orders(showTiming, user, seats.get(i), ticketStatus, product.get(), productDetails.get(i).getNumber(), productStatus, ticketsPrice, productsPrice, getCurrentDate());
                } else {
                    order = new Orders(showTiming, user, seats.get(i), ticketStatus, product.get(), productDetails.get(i).getNumber(), productStatus, 0, 0, getCurrentDate());
                }
                this.orderRepository.save(order);
            }
            for(int i = seats.size(); i < productDetails.size(); i++){
                Optional<Product> product = this.productRepository.findById(productDetails.get(i).getId());
                Orders order = new Orders(showTiming, user, null, ticketStatus, product.get(), productDetails.get(i).getNumber(), productStatus, 0, 0, getCurrentDate());
                this.orderRepository.save(order);
            }
        }

        List<DataSource> dataSourceList = new ArrayList<>();

        InputStreamResource file = getSeatsPdfContent(showTimingDto, seats, nrAdults, nrStudents, nrChilds,
                ticketsPrice, ticketsDiscount);
        InputStream inputStream = file.getInputStream();
        DataSource dataSource = new ByteArrayDataSource(inputStream, "application/pdf");
        dataSourceList.add(dataSource);

        if(productDetails.size() > 0) {
            InputStreamResource file1 = getProductsPdfContent(showTimingDto, productDetails, productsPrice, productsDiscount);
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
                            "Below you have attachments with booked tickets and booked foods and/or drinks.",
                    "Hot Movies Center - Booked Tickets And Products", user.getEmail(), dataSourceList);
        }
    }

    public List<SeatTicketStatusDto> findSeatsAndTicketsStatusByShowTiming(Long id){
        List<Object[]> objects = this.orderRepository.findSeatsAndTicketsStatusByShowTimingId(id);
        List<SeatTicketStatusDto> seatTicketStatusDtos = new ArrayList<>();
        for(Object[] object: objects){
            String seat = (String) object[0];
            String ticketStatus = (String) object[1];
            seatTicketStatusDtos.add(new SeatTicketStatusDto(seat, ticketStatus));
        }
        return seatTicketStatusDtos;
    }

    public Set<Long> filterOrders(List<Orders> orders, OrderPageDTO dto) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Orders> query = builder.createQuery(Orders.class);
        Root<Orders> root = query.from(Orders.class);
        Specification<Orders> specification = this.orderSpecification.getOrders(dto.getDto());

        Predicate predicate = specification.toPredicate(root, query, builder);
        query.where(predicate);

        Set<Long> filteredOrders = new HashSet<>();
        for (Orders order : orders) {
            Predicate orderPredicate = builder.and(predicate, builder.equal(root, order));
            query.where(orderPredicate);
            List<Orders> result = entityManager.createQuery(query).getResultList();
            if (!result.isEmpty()) {
                filteredOrders.add(order.getId());
            }
        }

        return filteredOrders;
    }

    public OrderPageResponseDto getAllOrdersByPaging(OrderPDto dto) {
        List<Object[]> objects = this.orderRepository
                .findOrdersOfAUser(
                        PageRequest.of(dto.getPage(), dto.getSize()),
                        dto.getUser().getId());
        List<OrdersDto> ordersDtos = new ArrayList<>();
        for(Object[] object: objects){
            ShowTiming showTiming =  (ShowTiming) object[0];
            String ticketsStatus = (String) object[1];
            long nrProducts = (long) object[2];
            String productsStatus = (String) object[3];
            Date date = (Date) object[4];
            /*float ticketsPrice = (float) object[5];
            float productsPrice = (float) object[6];*/
            //Orders orders2 = (Orders) object[5];
            Orders orders2 = this.orderRepository.findOrderByShowTiming(showTiming.getId());
            long nrTickets = getTicketsNumber(dto.getUser().getId(), showTiming.getId(), date);
            ordersDtos.add(new OrdersDto(this.modelMapper.map(showTiming, ShowTimingDto.class), dto.getUser(),
                    (int) nrTickets, ticketsStatus, (int) nrProducts, productsStatus,
                    orders2.getTicketsPrice(), orders2.getProductsPrice(), date));
        }
        int totalOrders = 0;
        if(this.orderRepository.findAll().size() > 0){
            totalOrders = this.orderRepository.findOrdersOfAUser(PageRequest.of(0, this.orderRepository.findAll().size()), dto.getUser().getId()).size();
        }
        return new OrderPageResponseDto(ordersDtos, totalOrders);
    }

    public OrderPageResponseDto getAllOrdersByPagingAndFilter(OrderPageDTO dto) {
        Set<Long> filteredOrders1 = filterOrders(this.orderRepository.findAll(), dto);
        int totalOrders = 0;
        List<OrdersDto> filteredOrders = new ArrayList<>();
        if(this.orderRepository.findAll().size() > 0){
            List<Object[]> objects = this.orderRepository
                    .findFilteredOrdersOfAUser(
                            PageRequest.of(dto.getPage(), dto.getSize()),
                            dto.getUser().getId(), filteredOrders1);
            for(Object[] object: objects){
                ShowTiming showTiming =  (ShowTiming) object[0];
                String ticketsStatus = (String) object[1];
                long nrProducts = (long) object[2];
                String productsStatus = (String) object[3];
                Date date = (Date) object[4];
                /*float ticketsPrice = (float) object[5];
                float productsPrice = (float) object[6];*/
                //Orders orders2 = (Orders) object[5];
                Orders orders2 = this.orderRepository.findOrderByShowTiming(showTiming.getId());
                long nrTickets = getTicketsNumber(dto.getUser().getId(), showTiming.getId(), date);
                filteredOrders.add(new OrdersDto(this.modelMapper.map(showTiming, ShowTimingDto.class), dto.getUser(),
                        (int) nrTickets, ticketsStatus, (int) nrProducts, productsStatus,
                        orders2.getTicketsPrice(), orders2.getProductsPrice(), date));
            }
            totalOrders = this.orderRepository.findFilteredOrdersOfAUser(PageRequest.of(0, this.orderRepository.findAll().size()), dto.getUser().getId(), filteredOrders1).size();
        }
        return new OrderPageResponseDto(filteredOrders, totalOrders);
    }

    public void changeOrdersStatus(OrdersDto ordersDto){
        List<Orders> orders = this.orderRepository.findOrdersByUserIdAndShowTimingIdAndCreatedDate(
                ordersDto.getUser().getId(), ordersDto.getShowTiming().getId(), ordersDto.getCreatedDate());
        if(ordersDto.getTicketsStatus() != null) {
            for(Orders order: orders) {
                order.setTicketStatus(ordersDto.getTicketsStatus());
                order.setCreatedDate(getCurrentDate());
                orderRepository.save(order);
            }
            if(ordersDto.getTicketsStatus().equals("cancelled")){
                refreshNumberOfAvailableProductsInTheatre(ordersDto);
            }
            sendEmailWithTicketsStatus(ordersDto);
        }
        if(ordersDto.getProductsStatus() != null) {
            for(Orders order: orders) {
                order.setProductsStatus(ordersDto.getProductsStatus());
                order.setCreatedDate(getCurrentDate());
                orderRepository.save(order);
            }
            if(ordersDto.getProductsStatus().equals("cancelled")){
                refreshNumberOfAvailableProductsInTheatre(ordersDto);
            }
            sendEmailWithBookedproductsStatus(ordersDto);
        }
    }

    public int getTicketsNumber(Long userId, Long showTimingId, Date createdDate){
        List<Orders> orders = this.orderRepository.findOrdersByUserIdAndShowTimingIdAndCreatedDate(
                userId, showTimingId, createdDate);
        int nr = 0;
        for(Orders order: orders){
            if(order.getSeat() != null){
                nr++;
            }
        }
        return nr;
    }

    public List<TicketDetailsDto> getTicketsDetails(OrdersDto ordersDto){
        List<Orders> orders = this.orderRepository.findOrdersByUserIdAndShowTimingIdAndCreatedDate(
                ordersDto.getUser().getId(), ordersDto.getShowTiming().getId(), ordersDto.getCreatedDate());
        List<TicketDetailsDto> ticketDetailsDtos = new ArrayList<>();
        for(Orders order: orders){
            if(order.getSeat() != null){
                ticketDetailsDtos.add(new TicketDetailsDto(order.getShowTiming().getVenue().getVenueNumber(), order.getShowTiming().getPrice(),
                         order.getSeat()));
            }
        }
        return ticketDetailsDtos;
    }

    public List<ProductDetailsDto> getBookedProductsDetails(OrdersDto ordersDto){
        List<Orders> orders = this.orderRepository.findOrdersByUserIdAndShowTimingIdAndCreatedDate(
                ordersDto.getUser().getId(), ordersDto.getShowTiming().getId(), ordersDto.getCreatedDate());
        List<ProductDetailsDto> productDetailsDtos = new ArrayList<>();
        for(Orders order: orders){
            if(order.getProduct() != null){
                productDetailsDtos.add(new ProductDetailsDto(order.getProduct().getName(), order.getProduct().getPrice(),
                        order.getProduct().getQuantity(), order.getNumberProducts()));
            }
        }
        return productDetailsDtos;
    }

    private String changeDateFormat(Date date){
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    private void sendEmailWithTicketsStatus(OrdersDto ordersDto){
        String subject = "Hot Movies Center - " + ordersDto.getTicketsStatus().substring(0, 1).toUpperCase() + ordersDto.getTicketsStatus().substring(1) + " Tickets";
        String body = "Your tickets for movie " + ordersDto.getShowTiming().getMovie().getName() +
                " at theatre " + ordersDto.getShowTiming().getTheatre().getName() + " on date of " +
                changeDateFormat(ordersDto.getShowTiming().getDay()) + " " +
                ordersDto.getShowTiming().getTime() + " were " + ordersDto.getTicketsStatus();
        this.emailService.sendEmail(subject, body, ordersDto.getUser().getEmail());
    }

    private void sendEmailWithBookedproductsStatus(OrdersDto ordersDto){
        String subject = "Hot Movies Center - " + ordersDto.getProductsStatus().substring(0, 1).toUpperCase() + ordersDto.getProductsStatus().substring(1) + " Products";
        String body = "Your products for movie " + ordersDto.getShowTiming().getMovie().getName() +
                " at theatre " + ordersDto.getShowTiming().getTheatre().getName() + " on date of " +
                changeDateFormat(ordersDto.getShowTiming().getDay()) + " " +
                ordersDto.getShowTiming().getTime() + " were " + ordersDto.getProductsStatus();
        this.emailService.sendEmail(subject, body, ordersDto.getUser().getEmail());
    }

    private void refreshNumberOfAvailableProductsInTheatre(OrdersDto ordersDto){
        List<Orders> orders = this.orderRepository.findOrdersByUserIdAndShowTimingIdAndCreatedDate(
                ordersDto.getUser().getId(), ordersDto.getShowTiming().getId(), ordersDto.getCreatedDate());
        for(Orders order: orders){
            Product product = order.getProduct();
            if(order.getProduct() != null){
                product.setNumber(product.getNumber() + order.getNumberProducts());
                this.productRepository.save(product);
            }
        }
    }

    public Date getLastOrderCreatedByUserAndShowTiming(UserShowTimingDto userShowTimingDto){
        return this.orderRepository.getLastOrderCreatedByUserAndShowTiming(userShowTimingDto.getUserId(), userShowTimingDto.getShowTimingId());
    }

    public List<TicketsNrDto> getTicketsNumber(){
        List<TicketsNrDto> ticketsNrDtos = new ArrayList<>();
        List<Object[]> results = this.orderRepository.findNumberOfTicketsPerMovie();
        List<Object[]> objects = results.subList(0, Math.min(results.size(), 4));
        for(Object[] object: objects){
            String movie = (String) object[0];
            long nr = (long) object[1];
            ticketsNrDtos.add(new TicketsNrDto(movie, (int) nr));
        }
        return ticketsNrDtos;
    }

    public List<TicketsPriceDto> getTicketsPrice(){
        List<TicketsPriceDto> ticketsPriceDtos = new ArrayList<>();
        List<Object[]> results = this.orderRepository.findPriceOfTicketsPerMovie();
        List<Object[]> objects = results.subList(0, Math.min(results.size(), 4));
        for(Object[] object: objects){
            String movie = (String) object[0];
            double price = (double) object[1];
            ticketsPriceDtos.add(new TicketsPriceDto(movie, price));
        }
        return ticketsPriceDtos;
    }

    public List<ProductsNrDto> getProductsNumber(){
        List<ProductsNrDto> productsNrDtos = new ArrayList<>();
        List<Object[]> objects = this.orderRepository.findNumberOfProductsSold();
        for(Object[] object: objects){
            String movie = (String) object[0];
            long nr = (long) object[1];
            productsNrDtos.add(new ProductsNrDto(movie, (int) nr));
        }
        return productsNrDtos;
    }
}
