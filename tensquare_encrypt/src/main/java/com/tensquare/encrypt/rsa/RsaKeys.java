package com.tensquare.encrypt.rsa;

/**
 * rsa加解密用的公钥和私钥
 * @author Administrator
 *
 */
public class RsaKeys {

	//生成秘钥对的方法可以参考这篇帖子
	//https://www.cnblogs.com/yucy/p/8962823.html

//	//服务器公钥
//	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Dw9nwjBmDD/Ca1QnRGy"
//											 + "GjtLbF4CX2EGGS7iqwPToV2UUtTDDemq69P8E+WJ4n5W7Iln3pgK+32y19B4oT5q"
//											 + "iUwXbbEaAXPPZFmT5svPH6XxiQgsiaeZtwQjY61qDga6UH2mYGp0GbrP3i9TjPNt"
//											 + "IeSwUSaH2YZfwNgFWqj+y/0jjl8DUsN2tIFVSNpNTZNQ/VX4Dln0Z5DBPK1mSskd"
//											 + "N6uPUj9Ga/IKnwUIv+wL1VWjLNlUjcEHsUE+YE2FN03VnWDJ/VHs7UdHha4d/nUH"
//											 + "rZrJsKkauqnwJsYbijQU+a0HubwXB7BYMlKovikwNpdMS3+lBzjS5KIu6mRv1GoE"
//											 + "vQIDAQAB";
//
//	//服务器私钥(经过pkcs8格式处理)
//	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDoPD2fCMGYMP8J"
//				 								 + "rVCdEbIaO0tsXgJfYQYZLuKrA9OhXZRS1MMN6arr0/wT5YniflbsiWfemAr7fbLX"
//				 								 + "0HihPmqJTBdtsRoBc89kWZPmy88fpfGJCCyJp5m3BCNjrWoOBrpQfaZganQZus/e"
//				 								 + "L1OM820h5LBRJofZhl/A2AVaqP7L/SOOXwNSw3a0gVVI2k1Nk1D9VfgOWfRnkME8"
//				 								 + "rWZKyR03q49SP0Zr8gqfBQi/7AvVVaMs2VSNwQexQT5gTYU3TdWdYMn9UeztR0eF"
//				 								 + "rh3+dQetmsmwqRq6qfAmxhuKNBT5rQe5vBcHsFgyUqi+KTA2l0xLf6UHONLkoi7q"
//				 								 + "ZG/UagS9AgMBAAECggEBANP72QvIBF8Vqld8+q7FLlu/cDN1BJlniReHsqQEFDOh"
//				 								 + "pfiN+ZZDix9FGz5WMiyqwlGbg1KuWqgBrzRMOTCGNt0oteIM3P4iZlblZZoww9nR"
//				 								 + "sc4xxeXJNQjYIC2mZ75x6bP7Xdl4ko3B9miLrqpksWNUypTopOysOc9f4FNHG326"
//				 								 + "0EMazVaXRCAIapTlcUpcwuRB1HT4N6iKL5Mzk3bzafLxfxbGCgTYiRQNeRyhXOnD"
//				 								 + "eJox64b5QkFjKn2G66B5RFZIQ+V+rOGsQElAMbW95jl0VoxUs6p5aNEe6jTgRzAT"
//				 								 + "kqM2v8As0GWi6yogQlsnR0WBn1ztggXTghQs2iDZ0YkCgYEA/LzC5Q8T15K2bM/N"
//				 								 + "K3ghIDBclB++Lw/xK1eONTXN+pBBqVQETtF3wxy6PiLV6PpJT/JIP27Q9VbtM9UF"
//				 								 + "3lepW6Z03VLqEVZo0fdVVyp8oHqv3I8Vo4JFPBDVxFiezygca/drtGMoce0wLWqu"
//				 								 + "bXlUmQlj+PTbXJMz4VTXuPl1cesCgYEA6zu5k1DsfPolcr3y7K9XpzkwBrT/L7LE"
//				 								 + "EiUGYIvgAkiIta2NDO/BIPdsq6OfkMdycAwkWFiGrJ7/VgU+hffIZwjZesr4HQuC"
//				 								 + "0APsqtUrk2yx+f33ZbrS39gcm/STDkVepeo1dsk2DMp7iCaxttYtMuqz3BNEwfRS"
//				 								 + "kIyKujP5kfcCgYEA1N2vUPm3/pNFLrR+26PcUp4o+2EY785/k7+0uMBOckFZ7GIl"
//				 								 + "FrV6J01k17zDaeyUHs+zZinRuTGzqzo6LSCsNdMnDtos5tleg6nLqRTRzuBGin/A"
//				 								 + "++xWn9aWFT+G0ne4KH9FqbLyd7IMJ9R4gR/1zseH+kFRGNGqmpi48MS61G0CgYBc"
//				 								 + "PBniwotH4cmHOSWkWohTAGBtcNDSghTRTIU4m//kxU4ddoRk+ylN5NZOYqTxXtLn"
//				 								 + "Tkt9/JAp5VoW/41peCOzCsxDkoxAzz+mkrNctKMWdjs+268Cy4Nd09475GU45khb"
//				 								 + "Y/88qV6xGz/evdVW7JniahbGByQhrMwm84R9yF1mNwKBgCIJZOFp9xV2997IY83S"
//				 								 + "habB/YSFbfZyojV+VFBRl4uc6OCpXdtSYzmsaRcMjN6Ikn7Mb9zgRHR8mPmtbVfj"
//				 								 + "B8W6V1H2KOPfn/LAM7Z0qw0MW4jimBhfhn4HY30AQ6GeImb2OqOuh3RBbeuuD+7m"
//				 								 + "LpFZC9zGggf9RK3PfqKeq30q";

	//服务器公钥
	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvx0dwCPKMZL46kCtYk0F" +
			"SrusKVkY4abAAWZrI72ywGauDDf7Jpydeph264eIdC+btFikwpYJmUK76mOrbpco" +
			"CzUVNRdLMb4Naqp23uni7PZCOkwvUhfBEZMvHZ9mEeuyE5gDXw5eJoMoUXdldbKQ" +
			"dstGWZzo53mhJXD+2MxCsnzNgzfuveReHlObME4G2z0YrpxEfWGzQZs3QEonfELj" +
			"UWMOPf4Qj2P/FgB5+dW4DZMq8xwBBVFcaKE94HXPjqXuTYDBzKzuU/NuTYBy94YI" +
			"dBvdmDx7MG1a35r1OfmZhm4gRw1l4SDZTcRadptJLwNGHtUk+YX6WDUMq/oq3jbS" +
			"zwIDAQAB";

	//服务器私钥(经过pkcs8格式处理)
	private static final String serverPrvKeyPkcs8 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC/HR3AI8oxkvjq" +
			"QK1iTQVKu6wpWRjhpsABZmsjvbLAZq4MN/smnJ16mHbrh4h0L5u0WKTClgmZQrvq" +
			"Y6tulygLNRU1F0sxvg1qqnbe6eLs9kI6TC9SF8ERky8dn2YR67ITmANfDl4mgyhR" +
			"d2V1spB2y0ZZnOjneaElcP7YzEKyfM2DN+695F4eU5swTgbbPRiunER9YbNBmzdA" +
			"Sid8QuNRYw49/hCPY/8WAHn51bgNkyrzHAEFUVxooT3gdc+Ope5NgMHMrO5T825N" +
			"gHL3hgh0G92YPHswbVrfmvU5+ZmGbiBHDWXhINlNxFp2m0kvA0Ye1ST5hfpYNQyr" +
			"+ireNtLPAgMBAAECggEAXQ5pWRBUOJVO3IlHo3DLXdtJ9gwrIFzYaxYanxmUOzkE" +
			"EHEFuoRR7crlRMQGvkQcNdFf0FvMKrMi2vtTExNI7UQYonRIKd1XnROJs/sAgs9Z" +
			"6hcEh52HZe5b57mgsqMoX4AKTDOrxmbgr+JKJegfjahnBeVwRp2jyJ5fiOLUqyFe" +
			"lW1KL46Ecpg1D+DbvCNXwR4LsPZD2Sckp8xJrLQt1bQ9uymNkxqEhoiOMMRcKm2C" +
			"5b7qdLCdIRIuxSHS1zaOC1seiLiQQtv8I4pMHSrGrZ8ZU1pxe0Ozh48BMrPWziEp" +
			"dWXVve+CLTSizOzcpBC5FeWLtoeoTmZ671T5FADsQQKBgQD8DZwy8kC8U9lsv44D" +
			"MV5R2cfzAoMdwuHG1Rl14+5th4cuEhXUWJXwv0z4x9UK90keZGrGKOFkhMJJ/JoA" +
			"5iKUpLMnYNUY6eGHIniYa0bs3yFVPE+g4fQWqc63YNfdAFNZ04U/IVqKK5+9/hSu" +
			"oHLvp99ZCybN0s4vGixrgaHiIQKBgQDCGzjSUscEtIPTtRe/2dneLj1ZEapLymo6" +
			"dgt7gji7DdjtkMcGYxp2pwMMUM/aVaj7hBSNdDYAI7vpmlp60yYEPx2qsRlT9HPt" +
			"ejkzo9EjnftBTUUyHwET6/jjQSoyJbFVTuIxUwnIrHcYuVETB04CEKN1bxhGJuSG" +
			"OV/e4an27wKBgBIkNKLiFtC3REX0f5cxPXWstByEiMVimneCLQP0NNwDTjJ9sL6I" +
			"5bnfOv4I/Ad/Bu8+EQVUOezPNWC34qm/5qtGM3LZIF1bsBTW4VEvH3q7EJ4e6ihN" +
			"OxFKhYtjWAYbGOS1ObjygJ7DRwsYt4peGAex0HyLNe7IhniTbLEMMoehAoGATGVu" +
			"bclKr11k7TQ/TrEtgLbuSlobjozyVmQTjN7p+SUwFgB/dNIYnk6JcpVz2VkRkzye" +
			"4jW080UdNoobxm/IbK9yu7XMZDC4V+VdEigNrlkFhOQmsd1mDTM42CjTtL0FGLBd" +
			"clBeb28ByOdXM4gSedmWnCfHhATWuMVoKE7lGF0CgYEAnllUw8lGrjk8iqEkPrCF" +
			"7lWmXOTzQG3Kp3lQjkUYn85UURuuXOcLfqeZRygJHdegya7yOPuqqB3CU1a/38th" +
			"+pH0BMbGnV4/CvMKML+4cCi2Iq2kx+R2zOA8s5wOXGO691lJPzn9teHJCjQ35p/3" +
			"NWp9p3arKx2CoPDF32P0wpg=";

	public static String getServerPubKey() {
		return serverPubKey;
	}

	public static String getServerPrvKeyPkcs8() {
		return serverPrvKeyPkcs8;
	}
	
}
