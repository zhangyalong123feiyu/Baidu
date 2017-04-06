package com.bibi.Baidu.Bean;

import java.io.Serializable;

public class MyResult extends Result {
	public MyData userinfo;

	public class MyData implements Serializable {

		public String avatar;
		public String nickName;
		public String age;
		public String sex;
		public String business;
		public String company;
		public String job;
		public String personSign;
		
		@Override
		public String toString() {
			return "MyData [avatar=" + avatar + ", nickName=" + nickName + ", age=" + age + ", sex=" + sex
					+ ", business=" + business + ", company=" + company + ", job=" + job + ", personSign=" + personSign
					+ "]";
		}
	}

	@Override
	public String toString() {
		return "MyResult [userinfo=" + userinfo + ", getFlag()=" + getFlag() + ", getMsg()=" + getMsg() + "]";
	}

}
