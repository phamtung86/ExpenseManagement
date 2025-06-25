package com.vti.config;

import com.vti.entity.User;
import com.vti.models.ReportModel;
import com.vti.service.ITransactionService;
import com.vti.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class ReportExpenseEveryDayScheduler {
    @Autowired
    private IUserService userService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ITransactionService transactionService;

    public String buildHtmlReport(String name,Map<Integer, ReportModel> data, LocalDate reportDate) {
        StringBuilder html = new StringBuilder();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        html.append("<!DOCTYPE html><html><body style=\"font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;\">");
        html.append("<div style=\"max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05);\">");
        html.append("<h2 style=\"color: #2d7dd2;\">Báo cáo chi tiêu ngày ")
                .append(reportDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("</h2>");
        html.append("<p>Chào ").append(name).append("</p><p>Dưới đây là báo cáo chi tiêu của bạn trong ngày hôm nay:</p>");
        html.append("<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">");
        html.append("<thead><tr style=\"background-color: #eaf4ff;\">");
        html.append("<th style=\"padding: 10px; border: 1px solid #ddd;\">STT</th>");
        html.append("<th style=\"padding: 10px; border: 1px solid #ddd;\">Danh mục</th>");
        html.append("<th style=\"padding: 10px; border: 1px solid #ddd;\">Số tiền</th>");
        html.append("<th style=\"padding: 10px; border: 1px solid #ddd;\">Loại</th>");
        html.append("</tr></thead><tbody>");

        int stt = 1;
        double totalExpense = 0;
        double totalIncome = 0;
        for (Map.Entry<Integer, ReportModel> entry : data.entrySet()) {
            ReportModel model = entry.getValue();
            if(model.getType().equals("EXPENSE")){
                totalExpense += model.getAmount();
            } else {
                totalIncome += model.getAmount();
            }

            html.append("<tr>");
            html.append("<td style=\"padding: 10px; border: 1px solid #ddd;\">").append(stt++).append("</td>");
            html.append("<td style=\"padding: 10px; border: 1px solid #ddd;\">").append(model.getName()).append("</td>");
            html.append("<td style=\"padding: 10px; border: 1px solid #ddd;\">")
                    .append(currencyFormat.format(model.getAmount())).append("</td>");
            html.append("<td style=\"padding: 10px; border: 1px solid #ddd;\">")
                    .append(model.getType().equalsIgnoreCase("EXPENSE") ? "Chi tiêu" : "Thu nhập").append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody></table>");
        html.append("<p style=\"margin-top: 20px;\"><strong>Tổng chi:</strong> ")
                .append(currencyFormat.format(totalExpense)).append("</p>");
        if(totalExpense > 0){
            html.append("<p style=\"margin-top: 20px;\"><strong>Tổng thu:</strong> ")
                .append(currencyFormat.format(totalIncome)).append("</p>");
        }
        html.append("<p style=\"margin-top: 20px;\">Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>");
        html.append("<p style=\"color: gray; font-size: 12px;\">Email này được gửi tự động. Vui lòng không trả lời.</p>");
        html.append("</div></body></html>");

        return html.toString();
    }

    public String buildEmptyHtmlReport(LocalDate reportDate) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><body style=\"font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;\">");
        html.append("<div style=\"max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05);\">");
        html.append("<h2 style=\"color: #2d7dd2;\">Báo cáo chi tiêu ngày ")
                .append(reportDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("</h2>");
        html.append("<p>Chào <b>Anh/Chị</b>,</p>");
        html.append("<p>Chúng tôi nhận thấy bạn chưa ghi nhận giao dịch nào trong ngày hôm nay.</p>");
        html.append("<p>Đừng quên ghi chép lại các khoản thu chi hàng ngày để dễ dàng theo dõi tài chính cá nhân nhé! 💰📘</p>");
        html.append("<ul style=\"padding-left: 20px;\">");
        html.append("<li>Ghi chép ngay sau khi chi tiêu hoặc nhận tiền.</li>");
        html.append("<li>Phân loại giao dịch rõ ràng để tiện thống kê.</li>");
        html.append("<li>Xem lại báo cáo định kỳ để kiểm soát ngân sách tốt hơn.</li>");
        html.append("</ul>");
        html.append("<p style=\"margin-top: 20px;\">Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>");
        html.append("<p style=\"color: gray; font-size: 12px;\">Email này được gửi tự động. Vui lòng không trả lời.</p>");
        html.append("</div></body></html>");

        return html.toString();
    }

    public void sendReportEmail(String toEmail, String subject, String htmlContent) throws MessagingException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true để gửi HTML

        javaMailSender.send(message);
    }

    @Scheduled(cron = "0 21 00 * * *")
    public void sendMailEveryDayReport() {
        try{
            LocalDate now = LocalDate.now();
            List<User> users = userService.getAllUsers();
            for (User u : users){
                Map<Integer, ReportModel> map = transactionService.getReport(u.getId());
                String html = "";
                if(map.isEmpty()){
                    html = buildEmptyHtmlReport(now);
                } else {
                    html = buildHtmlReport(u.getFullName(),map, now);
                }
                sendReportEmail(u.getEmail(), "Báo cáo chi tiêu ngày " + now, html);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
