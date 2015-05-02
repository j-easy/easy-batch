package org.easybatch.flatfile;

import java.util.Date;

public class Complaint {

    private int id;

    private String product;
    private String subProduct;

    private String issue;
    private String subIssue;

    private String state;

    private String zipCode;

    private Channel channel;

    private Date receivedDate;
    private Date sentDate;

    private String company;
    private String companyResponse;

    private boolean timelyResponse;
    private boolean consumerDisputed;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(String subProduct) {
        this.subProduct = subProduct;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSubIssue() {
        return subIssue;
    }

    public void setSubIssue(String subIssue) {
        this.subIssue = subIssue;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyResponse() {
        return companyResponse;
    }

    public void setCompanyResponse(String companyResponse) {
        this.companyResponse = companyResponse;
    }

    public boolean isTimelyResponse() {
        return timelyResponse;
    }

    public void setTimelyResponse(boolean timelyResponse) {
        this.timelyResponse = timelyResponse;
    }

    public boolean isConsumerDisputed() {
        return consumerDisputed;
    }

    public void setConsumerDisputed(boolean consumerDisputed) {
        this.consumerDisputed = consumerDisputed;
    }

}
