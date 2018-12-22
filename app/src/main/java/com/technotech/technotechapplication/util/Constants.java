package com.technotech.technotechapplication.util;


public class Constants {

	public  static String BasePath = "http://72.249.170.12/BizeeApi/api/";
	public static final String Bizee_login= BasePath + "User/UserLogin";
	public static final String Bizee_forgot= BasePath + "User/ForgotPassword";
	public static final String Bizee_Registration= BasePath + "User/Registration";
	public static final String Bizee_getbusinesstype= BasePath + "user/GetBussinessTypeList";
	public static final String Bizee_setpassword= BasePath + "User/SetPassword";
	public static final String Bizee_getsocial= BasePath + "Social/GetSocial";
	public static final String Bizee_setsocial= BasePath + "Social/InsertUpdateSocial";
	public static final String Bizee_all= BasePath + "User/GetUserAllBussinessDetails";
	public static final String Bizee_getfollower= BasePath + "Follow/GetFollowerList";
	public static final String Bizee_openinghours= BasePath + "User/SetTime";
	//public static final String getInfoByPincode="https://data.gov.in/api/datastore/resource.json?resource_id=6176ee09-3d56-4a3b-8115-21841576b2f6&api-key=a56beb163ec5ab6f5aab1e5734b05b4a&filters[pincode]=";
	public static final String getInfoByPincode=BasePath + "User/GetPinCodeDetails?pincode=";
	public static final String gettest="http://admin.spintra.com/api/Main/GetEnglishWord?EngWord=a";
	////database
	public final static String Database_Path = "/data/data/com.calica.bluetooth/databases/";
	public final static String Database_Name = "CEEl.db";
	public final static String usertable_Name = "userlogin";
	public static final String Get_BranchList = BasePath + "Branch/GetBranchList" ;
	public static final String Add_Branch = BasePath + "Branch/InsertBranch" ;
	public static final String Update_Branch = BasePath + "Branch/UpdateBranch" ;
	public static final String Delete_Branch = BasePath + "Branch/DeleteBranch" ;
	public static final int BRANCHLIST = 1;
	public static final int ADDBRANCH = 2;
	public static final int UPDATEBRANCH = 3;
	public static final int DELETEBRANCH = 4;
	public static final String b_Add_Product = BasePath + "Product/InsertProduct" ;
	public static final String b_Get_ProductList = BasePath + "Product/GetProductListByUser" ;
	public static final String b_Update_Product = BasePath + "Product/UpdateProduct" ;
	public static final String b_Delete_product = BasePath + "Product/DeleteProduct" ;

	public static final int PRODUCTLIST = 5;
	public static final int ADDPRODUCT = 6;
	public static final int UPDATEPRODUCT = 7;
	public static final int DELETEPRODUCT = 8;

	public static final String b_Get_BannertList = BasePath + "Banner/GetBanner" ;
	public static final String b_Add_Banner = BasePath + "Banner/InsertBanner" ;
	public static final String b_Delete_Banner = BasePath + "Banner/DeleteBanner" ;

	public static final int BANNERlIST = 9;
	public static final int ADDBANNER = 10;
	public static final int DELETEBANNER = 11;


	public static final String b_Get_CataloguetList = BasePath + "Catalogue/GetCatalogue" ;
	public static final String b_Add_Catalogue = BasePath + "Catalogue/InsertCatalogue" ;
	public static final String b_Delete_Catalogue = BasePath + "Catalogue/DeleteCatalogue" ;

	public static final int CATLOGUElIST = 12;
	public static final int ADDCATLOGUE = 13;
	public static final int DELETECATLOGUE = 14;


	public static final String b_Add_Offer = BasePath + "Offer/InsertOffer" ;
	public static final String b_Get_OfferList = BasePath + "Offer/GetOfferList" ;
	public static final String b_Update_Offer = BasePath + "Offer/UpdateOffer" ;
	public static final String b_Delete_Offer = BasePath + "Offer/DeleteOffer" ;

	public static final int OFFERLIST = 15;
	public static final int ADDOFFER = 16;
	public static final int UPDATEOFFER = 17;
	public static final int DELETEOFFER = 18;

	public static final int DEFAULT_TIMEOUT_MS = 10000;
	public static final int DEFAULT_MAX_RETRIES = 10;
	public static final float DEFAULT_BACKOFF_MULT = 1f;

	public static  boolean DEFAULT_Broadcasting = false;

	public static int Primary = 0;
	public static int Accent = 0;


}
