package com.cinema.models;

import java.math.BigDecimal;

public class Ticket {
    private int id;
    private int bookingId;
    private int seatId;
    private BigDecimal price;
    private String ticketCode;

    private int rowNumber;
    private int seatNumber;
    private String seatType;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }
    public int getRowNumber() { return rowNumber; }
    public void setRowNumber(int rowNumber) { this.rowNumber = rowNumber; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    @Override public String toString() {
        return "Ticket{code=" + ticketCode + ", seat=" + rowNumber + "-" + seatNumber +
                " (" + seatType + "), price=" + price + "}";
    }
}
