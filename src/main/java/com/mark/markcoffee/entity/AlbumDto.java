package com.mark.markcoffee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto implements Serializable {

	private Integer sourceId;//第三方id
	private String enterpriseCode;//企业唯一code
	private String imgPath;//图片路径
	private Integer status;//0:待审核、1:正常、2:删除
	private Integer categoryCode;//图片类型（0：logo、1：企业实景、2：工作环境、3：食堂宿舍、4：生活娱乐、5：企业周边、6其他 7:岗位图片，8：岗位主图，9：企业营业执照、10：企业封面图、11：工牌/名片、12:渠道商客户合作协议、13:渠道商一手招聘订单）

}
