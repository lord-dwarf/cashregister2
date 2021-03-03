package com.polinakulyk.cashregister2.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class XZReportResponseDto {
    private String reportId;
    private ReportKind reportKind;
    private String cashboxName;
    private String companyName = "Creamery";
    private LocalDateTime shiftStartTime;
    private LocalDateTime createdTime;
    private String createdBy;
    private Integer numReceiptsCompleted;
    private BigDecimal sumTotal;

    public XZReportResponseDto setReportId(String reportId) {
        this.reportId = reportId;
        return this;
    }

    public ReportKind getReportKind() {
        return reportKind;
    }

    public XZReportResponseDto setReportKind(ReportKind reportKind) {
        this.reportKind = reportKind;
        return this;
    }

    public XZReportResponseDto setCashboxName(String cashboxName) {
        this.cashboxName = cashboxName;
        return this;
    }

    public XZReportResponseDto setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public XZReportResponseDto setShiftStartTime(LocalDateTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
        return this;
    }

    public XZReportResponseDto setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public XZReportResponseDto setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public XZReportResponseDto setNumReceiptsCompleted(Integer numReceiptsCompleted) {
        this.numReceiptsCompleted = numReceiptsCompleted;
        return this;
    }

    public XZReportResponseDto setSumTotal(BigDecimal sumTotal) {
        this.sumTotal = sumTotal;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XZReportResponseDto that = (XZReportResponseDto) o;

        if (!reportId.equals(that.reportId)) return false;
        if (!reportKind.equals(that.reportKind)) return false;
        if (!cashboxName.equals(that.cashboxName)) return false;
        if (!companyName.equals(that.companyName)) return false;
        if (!shiftStartTime.equals(that.shiftStartTime)) return false;
        if (!createdTime.equals(that.createdTime)) return false;
        if (!createdBy.equals(that.createdBy)) return false;
        if (!numReceiptsCompleted.equals(that.numReceiptsCompleted)) return false;
        return sumTotal.equals(that.sumTotal);
    }

    @Override
    public int hashCode() {
        int result = reportId.hashCode();
        result = 31 * result + reportKind.hashCode();
        result = 31 * result + cashboxName.hashCode();
        result = 31 * result + companyName.hashCode();
        result = 31 * result + shiftStartTime.hashCode();
        result = 31 * result + createdTime.hashCode();
        result = 31 * result + createdBy.hashCode();
        result = 31 * result + numReceiptsCompleted.hashCode();
        result = 31 * result + sumTotal.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(
                ", ", XZReportResponseDto.class.getSimpleName() + "[", "]")
                .add("reportId='" + reportId + "'")
                .add("reportKind='" + reportKind + "'")
                .add("cashboxName='" + cashboxName + "'")
                .add("companyName='" + companyName + "'")
                .add("shiftStartTime=" + shiftStartTime)
                .add("createdTime=" + createdTime)
                .add("createdBy='" + createdBy + "'")
                .add("numReceiptsCompleted=" + numReceiptsCompleted)
                .add("sumTotal=" + sumTotal)
                .toString();
    }
}
