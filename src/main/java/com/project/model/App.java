//Model ứng dụng
package com.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "app")
public class App {
	 @Id
	 private Long id;
	
	 //Tên ứng dụng
	 private String appname;
	 //Thể loại
	 private String category;
	 //Đường dẫn file cài đặt
	 private String installfile;
	 //Phương thức trả phí
	 private String payment;
	 //Nhà phát triển
	 private String developer;
	 //Giá tiền
	 private int price;
	 //Mô tả ngắn
	 private String shortdescription;
	 //Dung lượng
	 private String filesize;
	 //Ngày đăng tải
	 private String uploaddate;
	 //Lượt tải
	 private int downloads;
	 //Đường dẫn icon hình ảnh của ứng dụng
	 private String iconimage;
	 //Mô tả chi tiết
	 private String detaildescription;
	 //Trạng thái
	 private String status;
	 //Ảnh screenshot từ 1 > 4
	 private String screenshot1;
	 private String screenshot2;
	 private String screenshot3;
	 private String screenshot4;
	 
	 
	 public App() {
	 }

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 public Long getId() {
	     return id;
	 }
	 
}
