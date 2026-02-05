package com.cinema.models;

import java.math.BigDecimal;

public class Booking {
    private int id;
    private int userId;
    private int showtimeId;
    private String bookingCode;
    private BigDecimal totalPrice;
    private String status;
    private String paymentStatus;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    @Override public String toString() {
        return "Booking{id=" + id + ", code=" + bookingCode + ", total=" + totalPrice +
                ", status=" + status + ", pay=" + paymentStatus + "}";
    }
}
