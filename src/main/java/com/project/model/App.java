//Model ứng dụng
package com.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

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
public class App {
	 @Id
	 private Long id;
	
	 //Tên ứng dụng
	 private String appname;
	 //Thể loại
	 private String category;
	 //Đường dẫn file cài đặt
	 private String installFilePath;
	 //Filter
	 private String filter;
	 //Nhà phát triển
	 private String developer;
	 //Giá tiền
	 private float price;
	 //Mô tả ngắn
	 private String shortDescription;
	 //Dung lượng
	 private String size;
	 //Ngày đăng tải
	 private String uploadDate;
	 //Lượt tải
	 private int downloads;
	 //Đường dẫn icon hình ảnh của ứng dụng
	 private String iconImage;
	 //Phương thức mua (trả phí hoặc miễn phí)
	 private String buyMethod;
	 //Mô tả chi tiết
	 private String detailDescription;
	 //Trạng thái
	 private String state;
	 
	 public App() {
	 }

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 public Long getId() {
	     return id;
	 }
	 
}
