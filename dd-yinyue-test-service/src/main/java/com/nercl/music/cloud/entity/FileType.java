package com.nercl.music.cloud.entity;

/**
 * 文件类型
 */
public enum FileType {

	VIDEO("wma,flash,mp4,mid,midi,3gp,mpeg,flv,mov,m4v,avi,dat,mkv,vob"), AUDIO(
			"cd,ogg,mp3,asf,wma,wav,mp3pro,rmvb,rm,real,ape,module,vqf,wmv"), DOCUMENT(
					"staff,num,drum,zip,rar,doc,docx,xls,xlsx,ppt,pptx,vsd,vsdx,mmp,txt,csv,pdf,xml,json,html,htm"), PICTURE(
							"jpg,bmp,pcx,tiff,gif,jpeg,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,png"), OTHER("");

	private String resourceType;

	private FileType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * 判断字符串是否是枚举指定的值,是则返回true
	 */
	public static final Boolean isDefined(String str) {
		FileType[] en = FileType.values();
		for (int i = 0; i < en.length; i++) {
			if (en[i].toString().equals(str)) {
				return true;
			}
		}
		return false;
	}

	public String getResourceTypeName() {
		return this.resourceType;
	}
}
