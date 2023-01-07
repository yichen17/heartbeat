package client.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2023/1/7 20:28
 * @describe 邮件通知dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailNoticeDTO {
    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String msg;

    private Boolean isHtml;


}
