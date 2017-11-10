package qrcode;

public class MakeQrcode {
		public static void main(String[] args) {
			try {
				QRCodeUtil.encode("http://www.zhaojinmao.cn/zhaojinmao/app/1.01.apk", "D:\\workSpace\\qrcode\\aa.png", "D:\\workSpace\\qrcode", "cc");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
