package com.technotech.technotechapplication.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Databasehelper extends SQLiteOpenHelper {
	private SQLiteDatabase myDataBase, myDataBase1;
	private Context context;

	public Databasehelper(Context context) {
		super(context, Constants.Database_Name, null, 1);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void createDatabase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				this.close();
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
//		SQLiteDatabase checkDB = null;
		boolean strBool = false;
		try {
			String myPath = Constants.Database_Path + Constants.Database_Name;
//			checkDB = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
			File dbFile = context.getDatabasePath(myPath);
			strBool = dbFile.exists();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return strBool;
//		if (checkDB != null) {
//			checkDB.close();
//		}
//		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {

		String outFileName = Constants.Database_Path + Constants.Database_Name;
		OutputStream myOutput = new FileOutputStream(outFileName);
		InputStream myInput = context.getAssets().open(Constants.Database_Name);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myInput.close();
		myOutput.flush();
		myOutput.close();
	}

	public void openDatabase() throws SQLException {
		String myPath = Constants.Database_Path + Constants.Database_Name;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public void closeDataBase() throws SQLException {
		myDataBase.close();
	}

	public void openDatabaseRead() throws SQLException {
		String myPath = Constants.Database_Path + Constants.Database_Name;
		myDataBase1 = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public void closeDataBaseRead() throws SQLException {
		myDataBase1.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public long adduserlogin(JSONObject j_obj) {
		// NOTE : function to insert or update location data.
		openDatabase();
		long id = 0;
		try {
			ContentValues cv = new ContentValues();
			cv.put("username", j_obj.getString("UserName"));
			cv.put("password", j_obj.getString("Password"));
			cv.put("verified", j_obj.getString("verified"));
			id = myDataBase.insert(Constants.usertable_Name, null, cv);
			Log.i("insert Location : ", String.valueOf(id));
		} catch (Exception e) {
			Log.i("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
		return id;
	}

	public JSONArray getuserlogin() {
		openDatabase();
		JSONArray resultSet = new JSONArray();
		try {
			String sql = "select * from " + Constants.usertable_Name;
			Cursor cursor = myDataBase.rawQuery(sql, null);
			if(cursor!=null) {
				resultSet = cur2Json(cursor);
			}else
			{
				resultSet=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}
	public JSONArray getenglish() {
		openDatabase();
		JSONArray resultSet = new JSONArray();
		try {
			String sql = "select ew.EngId as 'EngId',ew.EngWord as 'EngWord',ew.ParentId as 'ParentId',ew.[Type] as 'Type',t.EngId as 'TranslationEngId',t.SpanishId as 'SpanishId' from EnglishWord as ew\n" +
					"left outer join  Translation as t on t.EngId = (case\n" +
					"When ew.ParentId=0 then ew.EngId\n" +
					"else ew.ParentId end)\n" +
					"where ew.EngId=1 ";
			Cursor cursor = myDataBase.rawQuery(sql, null);
			if(cursor!=null) {
				resultSet = cur2Json(cursor);
			}else
			{
				resultSet=null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public JSONArray cur2Json(Cursor cursor) {

		JSONArray resultSet = new JSONArray();
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			int totalColumn = cursor.getColumnCount();
			JSONObject rowObject = new JSONObject();
			for (int i = 0; i < totalColumn; i++) {
				if (cursor.getColumnName(i) != null) {
					try {
						rowObject.put(cursor.getColumnName(i),
								cursor.getString(i));
					} catch (Exception e) {

					}
				}
			}
			resultSet.put(rowObject);
			cursor.moveToNext();
		}

		cursor.close();
		return resultSet;

	}
	public void deleteuserlogin() {
		openDatabase();
		JSONArray resultSet = new JSONArray();
		try {
			String sql = "delete from " + Constants.usertable_Name;
			 myDataBase.delete(Constants.usertable_Name,null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



	/*public ArrayList<String[]> getAllGroupMembersByGroupId(String g_Id) {
		ArrayList<String[]> groupMemberList = null;
		// NOTE : function to insert or update location data.
		openDatabase();
		try {
			String sql = "select * from " + Constants.GroupMemberMaster_table
					+ " where "+Constants.groupid+" = "+g_Id;
			Cursor cr = myDataBase.rawQuery(sql, null);
			groupMemberList=new ArrayList<>(cr.getCount());
			if(cr.moveToFirst())
			{
				do{
					groupMemberList.add(new String[]{cr.getString(0),cr.getString(1),cr.getString(2),cr.getString(3),cr.getString(4),cr.getString(5),cr.getString(6),cr.getString(7)});
				}while (cr.moveToNext());
			}


			cr.close();
		} catch (Exception e) {
			Log.e("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
		return groupMemberList;
	}*/



//		// TODO Auto-generated method stub
//		openDatabase();
//		int cnt = 0;
//		try {
//			String sql = "select count(*) from " + constants.Table_Menuitem
//					+ "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToPosition(0);
//				cnt = cr.getInt(0);
//			}
//			cr.close();
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return cnt;
//	}


	/*public void InsertLocation(clsLocation clsloc) {
		// NOTE : function to insert or update location data.
		openDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("LocationId", clsloc.LocationId);
			cv.put("Location_name", clsloc.Location_name);
			cv.put("Address", clsloc.Address);
			cv.put("Country", clsloc.Country);
			cv.put("State", clsloc.State);
			cv.put("City", clsloc.City);
			cv.put("Latitude", clsloc.Latitude);
			cv.put("Longitude", clsloc.Longitude);
			cv.put("Phone", clsloc.Phone);
			cv.put("Mobile", clsloc.Mobile);
			cv.put("Fax", clsloc.Fax);
			cv.put("Zipcode", clsloc.Zipcode);
			cv.put("Email", clsloc.Email);
			cv.put("bookingtimelimit", clsloc.bookingtimelimit);
			cv.put("ordersubmittime", clsloc.ordersubmittime);
			long id;
			
			Log.i("Database", " "+ cv);
			id = myDataBase.insert(constants.Table_Location, null, cv);
			Log.i("insert Location : ", String.valueOf(id));
		} catch (Exception e) {
			Log.i("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
	}*/

	/*public void DeleteLocationTime() {
		openDatabase();
		try {
			myDataBase.delete(constants.Table_LocationTime, null, null);
		} catch (Exception e) {
			Log.i("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
	}*/

	/*public void InsertLocationTime(clsLocationTime clsloctime) {
		// NOTE : function to insert or update location time.
		openDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("locationid", clsloctime.LocationId);
			cv.put("nameday", clsloctime.NameDay);
			cv.put("openingtime", clsloctime.OpeningTime);
			cv.put("openingtimemeridian", clsloctime.OpeningTimeMeridian);
			cv.put("closingtime", clsloctime.ClosingTime);
			cv.put("closingtimemeridian", clsloctime.ClosingTimeMeridian);
			cv.put("weekname", clsloctime.WeekName);
			long id;
			id = myDataBase.insert(constants.Table_LocationTime, null, cv);
			Log.i("insert Location Time: ", String.valueOf(id));
		} catch (Exception e) {
			Log.i("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
	}*/

	/*public int GetCategoryCount() {
		// TODO Auto-generated method stub
		openDatabase();
		int cnt = 0;
		try {
			String sql = "select count(*) from " + constants.Table_Category
					+ "";
			Cursor cr = myDataBase.rawQuery(sql, null);
			if (cr != null && cr.getCount() > 0) {
				cr.moveToPosition(0);
				cnt = cr.getInt(0);
			}
			cr.close();
		} catch (Exception e) {
			Log.i("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
		return cnt;
	}*/

	/*public void DeleteCategory() {
		openDatabase();
		try {
			myDataBase.delete(constants.Table_Category, null, null);
		} catch (Exception e) {
			Log.i("Exe : ", e.getMessage());
			e.printStackTrace();
		}
		closeDataBase();
		SQLiteDatabase.releaseMemory();
	}*/







//	public void DeleteCategory() {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Category, null, null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertCategory(clsCategory clscat) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("row_id", clscat.RowId);
//			cv.put("cat_id", clscat.CategoryId);
//			cv.put("cat_name", clscat.CategoryTitle);
//			cv.put("description", clscat.CategoryDescription);
//			cv.put("cat_parent_id", clscat.CategoryParentId);
//			cv.put("isLast", clscat.IsLast);
//			long id;
//			id = myDataBase.insert(constants.Table_Category, null, cv);
//			Log.i("insert Category : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public int GetMenuItemCount() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		int cnt = 0;
//		try {
//			String sql = "select count(*) from " + constants.Table_Menuitem
//					+ "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToPosition(0);
//				cnt = cr.getInt(0);
//			}
//			cr.close();
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return cnt;
//	}
//
//	public void DeleteMenuItem() {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Menuitem, null, null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertMenuItem(clsMenuItem clsmenuitem) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("row_id", clsmenuitem.RowId);
//			cv.put("menuitemId", clsmenuitem.MenuItemId);
//			cv.put("catId", clsmenuitem.CategoryId);
//			cv.put("description", clsmenuitem.Description);
//			cv.put("imgurl", clsmenuitem.ImgURL);
//			cv.put("menuItemName", clsmenuitem.MenuItemName);
//			cv.put("thumbimgurl", clsmenuitem.ThumbimgURL);
//			cv.put("totaldislikes", clsmenuitem.Totaldislikes);
//			cv.put("totallikes", clsmenuitem.Totallikes);
//			long id;
//			id = myDataBase.insert(constants.Table_Menuitem, null, cv);
////			Log.i("insert MenuItem : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void DeleteMenuItemById(int Id) {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_CartTopping, " MenuItemId=" + Id
//					+ "", null);
//			myDataBase.delete(constants.Table_Cart, " MenuItemId=" + Id + "",
//					null);
//			myDataBase.delete(constants.Table_LikeDislike, " menuItemId=" + Id
//					+ "", null);
//			myDataBase.delete(constants.Table_Status, " menuitemId=" + Id + "",
//					null);
//			myDataBase.delete(constants.Table_Price_Toppings, " menuitemId="
//					+ Id + "", null);
//			myDataBase.delete(constants.Table_Menuitem, " menuitemId=" + Id
//					+ "", null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void UpdatePriceTopping(clsPriceToppings clspricetop) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("menuitemId", clspricetop.menuitemId);
//			cv.put("price", clspricetop.price);
//			cv.put("type", clspricetop.type);
//			cv.put("typeid", clspricetop.typeid);
//			cv.put("isToppings", clspricetop.isToppings);
//			long id;
//			if (clspricetop.LocationId == 0) {
//				id = myDataBase.update(constants.Table_Price_Toppings, cv," menuitemId=" + clspricetop.menuitemId + " and typeid=" + clspricetop.typeid + "",null);
//			} else {
//				id = myDataBase.update(constants.Table_Price_Toppings, cv," menuitemId=" + clspricetop.menuitemId + " and LocationId=" + clspricetop.LocationId + " and typeid=" + clspricetop.typeid + "",null);
//			}
////			Log.i("insert Price Topping : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public int GetPriceTopping() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		int cnt = 0;
//		try {
//			String sql = "select count(*) from "
//					+ constants.Table_Price_Toppings + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToPosition(0);
//				cnt = cr.getInt(0);
//			}
//			cr.close();
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return cnt;
//	}
//
//	public void DeletePriceTopping() {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Price_Toppings, null, null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertPriceTopping(clsPriceToppings clspricetop) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("menuitemId", clspricetop.menuitemId);
//			cv.put("price", clspricetop.price);
//			cv.put("type", clspricetop.type);
//			cv.put("typeid", clspricetop.typeid);
//			cv.put("isToppings", clspricetop.isToppings);
//			cv.put("LocationId", clspricetop.LocationId);
//			long id;
//			id = myDataBase.insert(constants.Table_Price_Toppings, null, cv);
////			Log.i("insert Price Topping : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void DeleteStatus() {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Status, null, null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertStatus(clsStatus clsstatus) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("menuitemId", clsstatus.MenuitemId);
//			cv.put("title", clsstatus.Title);
//			cv.put("iconurl", clsstatus.Iconurl);
//			long id;
//			id = myDataBase.insert(constants.Table_Status, null, cv);
////			Log.i("insert Status : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void DeleteRestaurant() {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Restaurant, null, null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertRestaurant(clsRestaurant clsrest) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("restaurantId", clsrest.RestaurantId);
//			cv.put("ownerId", clsrest.OwnerId);
//			cv.put("restaurantName", clsrest.RestaurantName);
//			cv.put("logourl", clsrest.Logourl);
//			cv.put("AboutUs", clsrest.AboutUs);
//			cv.put("updateddate", clsrest.Updateddate);
//			cv.put("currencycode", clsrest.Currencycode);
//			cv.put("currencysymbol", clsrest.Currencysymbol);
//			cv.put("hasmultipleprice", clsrest.Hasmultipleprice);
//			cv.put("iswaiting", clsrest.Iswaiting);
//			cv.put("isdelivery", clsrest.Isdelivery);
//			cv.put("istakeaway", clsrest.Istakeaway);
//			cv.put("cultureName", clsrest.CultureName);
//			cv.put("ISDCode", clsrest.ISDCode);
//			cv.put("facebook", clsrest.Facebook);
//			cv.put("twitter", clsrest.Twitter);
//			cv.put("linkedin", clsrest.Linkedin);
//			long id;
//			id = myDataBase.insert(constants.Table_Restaurant, null, cv);
////			Log.i("insert Restaurant : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void DeleteMessage() {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Alert_Message, null, null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertMessage(clsAlertMessage clsmsg) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("messageId", clsmsg.MessageId);
//			cv.put("title", clsmsg.Title);
//			cv.put("message", clsmsg.Message);
//			long id;
//			id = myDataBase.insert(constants.Table_Alert_Message, null, cv);
////			Log.i("insert Message : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertCart(clsCart clscart,
//			List<HashMap<String, String>> objListTopping) {
//		long id = 0;
//		int maxid = 1;
//		openDatabase();
//		try {
//			Cursor c1 = myDataBase
//					.rawQuery("select max(cartid) as cartid from "
//							+ constants.Table_Cart, null);
//			c1.moveToFirst();
//			if (c1 != null) {
//				maxid = maxid + c1.getInt(0);
//			}
//			int qty = clscart.Quantity;
//			boolean flg = false;
//			int cartid = 0, qtyspc = 0;
//			Cursor c = myDataBase.rawQuery("select quantity,cartid from "
//					+ constants.Table_Cart + " where menuitemid= "
//					+ clscart.MenuItemId + " and price_typeid="
//					+ clscart.PriceTypeId + " and IsOffer=0", null);
//			c.moveToFirst();
//			int cntItem = 0;
//			if (c != null && c.getCount() > 0) {
//				for (int i = 0; i < c.getCount(); i++) {
//					c.moveToPosition(i);
//					if (objListTopping.size() > 0) {
//						int cnt = 0;
//						Cursor cTop = myDataBase.rawQuery(
//								"select top_typeId from "
//										+ constants.Table_CartTopping
//										+ " where menuitemid="
//										+ clscart.MenuItemId + " and cartid="
//										+ c.getInt(1), null);
//						cTop.moveToFirst();
//						if (cTop != null && cTop.getCount() > 0) {
//							for (int j = 0; j < cTop.getCount(); j++) {
//								cTop.moveToPosition(j);
//								for (HashMap<String, String> entry : objListTopping) {
//									if (Integer.parseInt(cTop.getString(0)) == Integer
//											.parseInt(entry.get("id"))) {
//										cnt = cnt + 1;
//									}
//								}
//							}
//						}
//						if (objListTopping.size() == cnt
//								&& cTop.getCount() == cnt) {
//							cnt = 0;
//							flg = true;
//							qty = clscart.Quantity + c.getInt(0);
//							maxid = c.getInt(1);
//							break;
//						}
//						cTop.close();
//					} else {
//						// Note: chk top exist in current item
//						Cursor ChkTop = myDataBase.rawQuery(
//								"select count(*) as cnt from "
//										+ constants.Table_CartTopping
//										+ " where menuitemid="
//										+ clscart.MenuItemId + " and cartid="
//										+ c.getInt(1), null);
//						ChkTop.moveToFirst();
//						if (ChkTop.getInt(0) == 0) {
//							cartid = c.getInt(1);
//							qtyspc = c.getInt(0);
//						}
//						// Note: chk top exist in current item
//						cntItem = cntItem + 1;
//					}
//				}
//			}
//			if (c.getCount() > 0 && c.getCount() == cntItem) {
//				cntItem = 0;
//				flg = true;
//				qty = clscart.Quantity + qtyspc;
//				maxid = cartid;
//				// break;
//			}
//
//			ContentValues cv = new ContentValues();
//			cv.put("cartid", maxid);
//			cv.put("MenuItemId", clscart.MenuItemId);
//			cv.put("catId", clscart.CategoryId);
//			// cv.put("top_typeId", clscart.TopTypeId);
//			cv.put("price_typeId", clscart.PriceTypeId);
//			cv.put("locationId", clscart.LocationId);
//			cv.put("quantity", qty);
//			cv.put("userId", clscart.UserId);
//			cv.put("date", clscart.date);
//			cv.put("IsOffer", clscart.IsOffer);
//			// Log.i("c",String.valueOf(c.getCount()));
//
//			if (flg) {
//				id = myDataBase.update(constants.Table_Cart, cv, "menuitemid= "
//						+ clscart.MenuItemId + " and cartid=" + maxid, null);
//				Log.i("update Cart : ", String.valueOf(id));
//			} else {
//				id = (int) myDataBase.insert(constants.Table_Cart, null, cv);
//				Log.i("insert Cart : ", String.valueOf(id));
//				for (HashMap<String, String> entry : objListTopping) {
//					ContentValues cv1 = new ContentValues();
//					cv1.put("cartid", maxid);
//					cv1.put("menuItemId", clscart.MenuItemId);
//					cv1.put("top_typeId", entry.get("id"));
//					cv1.put("price", entry.get("price"));
//					myDataBase.insert(constants.Table_CartTopping, null, cv1);
//					Log.i("insert Cart Topping : ", String.valueOf(id));
//				}
//			}
//			c.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		// return maxid;
//	}
//
//	public void InsertUpdateCartTopping(clsCartTopping clscarttopping) {
//		openDatabase();
//		try {
//			Cursor c = myDataBase.rawQuery("select count(*) from "
//					+ constants.Table_CartTopping + " where menuitemid= "
//					+ clscarttopping.MenuItemId + " and top_typeId="
//					+ clscarttopping.top_typeId, null);
//			c.moveToFirst();
//			if (c.getInt(0) == 0) {
//				ContentValues cv = new ContentValues();
//				cv.put("cartid", clscarttopping.cartid);
//				cv.put("menuItemId", clscarttopping.MenuItemId);
//				cv.put("top_typeId", clscarttopping.top_typeId);
//				cv.put("price", clscarttopping.price);
//				myDataBase.insert(constants.Table_CartTopping, null, cv);
//				c.close();
//			} else {
//				myDataBase.delete(constants.Table_CartTopping, "menuitemid= "
//						+ clscarttopping.MenuItemId + " and top_typeId="
//						+ clscarttopping.top_typeId, null);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertCartOffer(clsCart clscart, clsOffer clsoffer) {
//		long id = 0;
//		int maxid = 1;
//		openDatabase();
//		try {
//			Cursor c1 = myDataBase
//					.rawQuery("select max(cartid) as cartid from "
//							+ constants.Table_Cart, null);
//			c1.moveToFirst();
//			if (c1 != null) {
//				maxid = maxid + c1.getInt(0);
//			}
//			int qty = clscart.Quantity;
//			boolean flg = false;
//			// int cartid=0,qtyspc=0;
//			Cursor c = myDataBase.rawQuery("select quantity,cartid from "
//					+ constants.Table_Cart + " where menuitemid= "
//					+ clscart.MenuItemId + " and locationId="
//					+ clscart.LocationId + " and IsOffer=1", null);
//			c.moveToFirst();
//			// int cntItem=0;
//			if (c != null && c.getCount() > 0) {
//				c.moveToFirst();
//				maxid = c.getInt(1);
//				qty = qty + c.getInt(0);
//				flg = true;
//			}
//
//			ContentValues cv = new ContentValues();
//			cv.put("cartid", maxid);
//			cv.put("MenuItemId", clscart.MenuItemId);
//			cv.put("catId", clscart.CategoryId);
//			cv.put("price_typeId", clscart.PriceTypeId);
//			cv.put("locationId", clscart.LocationId);
//			cv.put("quantity", qty);
//			cv.put("userId", clscart.UserId);
//			cv.put("date", clscart.date);
//			cv.put("IsOffer", clscart.IsOffer);
//
//			if (flg) {
//				id = myDataBase.update(constants.Table_Cart, cv, "menuitemid= "
//						+ clscart.MenuItemId + " and cartid=" + maxid, null);
//				Log.i("update Cart : ", String.valueOf(id));
//			} else {
//				id = (int) myDataBase.insert(constants.Table_Cart, null, cv);
//				Log.i("insert Cart Offer : ", String.valueOf(id));
//
//				ContentValues cvOffer = new ContentValues();
//				cvOffer.put("cartid", maxid);
//				cvOffer.put("offertitle", clsoffer.OfferTitle);
//				cvOffer.put("description", clsoffer.OfferDtls);
//				cvOffer.put("redeempoint", clsoffer.OfferPoint);
//				myDataBase.insert(constants.Table_CartOffer, null, cvOffer);
//				Log.i("insert Cart Offer : ", String.valueOf(id));
//			}
//			c.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		// return maxid;
//	}
//
//	public float GetTotalToppingPrice() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		float result = 0;
//		try {
//			String sql = "select sum(price) as price from "
//					+ constants.Table_CartTopping + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToPosition(0);
//				result = cr.getFloat(0);
//			}
//			cr.close();
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return result;
//	}
//
//	public ArrayList<clsShopCart> RetriveCart(int locid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsShopCart> arrlist = null;
//		try {
//			String sql = "select c.menuitemid,m.menuItemName,p.price,c.price_typeid,c.quantity,m.imgurl,P.type,c.cartid from "
//					+ constants.Table_Cart
//					+ " c inner join "
//					+ constants.Table_Menuitem
//					+ " m on c.menuitemid=m.menuitemid inner join "
//					+ constants.Table_Price_Toppings
//					+ " p on p.menuitemid=c.menuitemid and p.typeid=c.price_typeid where c.IsOffer=0 and (p.LocationId="+ locid + " or p.LocationId=0) order by c.date";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//
//				arrlist = new ArrayList<clsShopCart>();
//
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsShopCart objCart = new clsShopCart();
//					objCart.MenuItemId = cr.getInt(0);
//					objCart.MenuItemName = cr.getString(1);
//					objCart.price = (float) cr.getDouble(2);
//					objCart.typeid = cr.getInt(3);
//					objCart.qty = cr.getInt(4);
//					objCart.ImgURL = cr.getString(5);
//					objCart.type = cr.getString(6);
//					objCart.cartid = cr.getInt(7);
//					arrlist.add(objCart);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public ArrayList<clsShopcartOffer> RetriveCartOffer(int locid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsShopcartOffer> arrlist = null;
//		try {
//			String sql = "select c.cartid,c.menuitemid,co.offertitle,co.redeempoint,c.quantity from "
//					+ constants.Table_Cart
//					+ " c inner join "
//					+ constants.Table_CartOffer
//					+ " co on co.cartId=c.cartId "
//					+ " where c.IsOffer=1 and c.LocationId="
//					+ locid
//					+ " order by c.date";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				arrlist = new ArrayList<clsShopcartOffer>();
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsShopcartOffer objCartOffer = new clsShopcartOffer();
//					objCartOffer.cartId = cr.getInt(0);
//					objCartOffer.offerId = cr.getInt(1);
//					objCartOffer.offerTitle = cr.getString(2);
//					objCartOffer.rewardPoint = cr.getInt(3);
//					objCartOffer.qty = cr.getInt(4);
//					arrlist.add(objCartOffer);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public ArrayList<clsCartTopping> RetriveCartTopping(int cartid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsCartTopping> arrlist = null;
//		try {
//			String sql = "select pt.menuitemid,pt.typeid,pt.price,pt.type from "
//					+ constants.Table_CartTopping
//					+ " c inner join "
//					+ constants.Table_Price_Toppings
//					+ " pt on c.menuitemid=pt.menuitemid and c.top_typeid=pt.typeid where c.cartid="
//					+ cartid + " and isToppings=1 order by pt.type";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				arrlist = new ArrayList<clsCartTopping>();
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsCartTopping objCart = new clsCartTopping();
//					objCart.MenuItemId = cr.getInt(0);
//					objCart.top_typeId = cr.getInt(1);
//					objCart.price = (float) cr.getDouble(2);
//					objCart.cartid = cartid;
//					objCart.toptypename = cr.getString(3);
//					arrlist.add(objCart);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public boolean chkPurchasedItem(int MenuItemId, int CategoryId,
//			int price_typeId, List<HashMap<String, String>> top_typeId) {
//		openDatabase();
//		boolean result = false;
//		try {
//			// Note: check in cart table
//			Cursor c = myDataBase.rawQuery("select cartid from "
//					+ constants.Table_Cart + " where menuitemid= " + MenuItemId
//					+ " and catid=" + CategoryId + " and price_typeId="
//					+ price_typeId + "", null);
//			c.moveToFirst();
//			if (c != null && c.getCount() > 0) {
//				for (int j = 0; j < c.getCount(); j++) {
//					c.moveToPosition(j);
//					for (HashMap<String, String> i : top_typeId) {
//						Cursor c1 = myDataBase.rawQuery("select Count(*) from "
//								+ constants.Table_CartTopping
//								+ " where menuitemid= " + MenuItemId
//								+ " and top_typeId=" + i.get("id")
//								+ " and cartid=" + c.getInt(0), null);
//						c1.moveToFirst();
//						if (c1 != null && c1.getCount() > 0) {
//							result = true;
//						} else {
//							result = false;
//							break;
//						}
//						c1.close();
//					}
//
//					// Note: check in cartTopping table to purchased topping is
//					// same or not
//					int totalTopping = 0;
//					Cursor c2 = myDataBase.rawQuery(
//							"select Count(*) as cnt from "
//									+ constants.Table_CartTopping
//									+ " where menuitemid= " + MenuItemId
//									+ " and cartid=" + c.getInt(0), null);
//					c2.moveToFirst();
//					totalTopping = c2.getInt(0);
//					if (totalTopping < top_typeId.size()) {
//						result = false;
//					}
//				}
//			}
//			c.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return result;
//	}
//
//	public float getCartTop() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		float result = 0;
//		try {
//			String sql = "select count(*) as price from "
//					+ constants.Table_CartTopping + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToPosition(0);
//				result = cr.getFloat(0);
//			}
//			cr.close();
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return result;
//	}
//
//	public clsUser RetriveCustomerDtls(int custId) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsUser objUser = null;
//		try {
//			String sql = "select customerId,firstname,lastname,deliveryaddress,totalrewardpoints,emailid,mobile,phone,fax,isGuest,isActive from "
//					+ constants.Table_User + " where customerId=" + custId + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				objUser = new clsUser();
//				objUser.customerId = cr.getInt(0);
//				objUser.firstname = cr.getString(1);
//				objUser.lastname = cr.getString(2);
//				objUser.deliveryaddress = cr.getString(3);
//				objUser.totalrewardpoints = cr.getInt(4);
//				objUser.emailid = cr.getString(5);
//				objUser.mobile = cr.getString(6);
//				objUser.phone = cr.getString(7);
//				objUser.fax = cr.getString(8);
//				if (cr.getInt(9) == 1) {
//					objUser.isGuest = true;
//				} else {
//					objUser.isGuest = false;
//				}
//				if (cr.getInt(10) == 1) {
//					objUser.isActive = true;
//				} else {
//					objUser.isActive = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objUser;
//	}
//
//	public void UpdateCustomer(clsUser clsuser) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("customerId", clsuser.customerId);
//			cv.put("firstname", clsuser.firstname);
//			cv.put("lastname", clsuser.lastname);
//			cv.put("deliveryaddress", clsuser.deliveryaddress);
//			cv.put("totalrewardpoints", clsuser.totalrewardpoints);
//			cv.put("emailid", clsuser.emailid);
//			cv.put("mobile", clsuser.mobile);
//			cv.put("phone", clsuser.phone);
//			cv.put("fax", clsuser.fax);
//			cv.put("isGuest", clsuser.isGuest);
//			cv.put("password", clsuser.password);
//			cv.put("isActive", clsuser.isActive);
//			long id;
//			id = myDataBase.update(constants.Table_User, cv, " customerId="
//					+ clsuser.customerId + "", null);
//			Log.i("update User : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void UpdateCustomerVerification(int custId) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("isActive", true);
//			long id;
//			id = myDataBase.update(constants.Table_User, cv, " customerId="
//					+ custId + "", null);
//			Log.i("update User : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertCustomer(clsUser clsuser) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("customerId", clsuser.customerId);
//			cv.put("firstname", clsuser.firstname);
//			cv.put("lastname", clsuser.lastname);
//			cv.put("deliveryaddress", clsuser.deliveryaddress);
//			cv.put("totalrewardpoints", clsuser.totalrewardpoints);
//			cv.put("emailid", clsuser.emailid);
//			cv.put("mobile", clsuser.mobile);
//			cv.put("phone", clsuser.phone);
//			cv.put("fax", clsuser.fax);
//			cv.put("isGuest", clsuser.isGuest);
//			cv.put("password", clsuser.password);
//			cv.put("isActive", clsuser.isActive);
//			long id;
//			id = myDataBase.insert(constants.Table_User, null, cv);
//			Log.i("insert User : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public String RetriveWholeCart() {
//		String result = "";
//		// TODO Auto-generated method stub
//		openDatabase();
//		try {
//			String sql = "select c.menuitemid,m.menuItemName,p.price,c.price_typeid,c.quantity,m.imgurl,P.type,c.cartid from "
//					+ constants.Table_Cart
//					+ " c inner join "
//					+ constants.Table_Menuitem
//					+ " m on c.menuitemid=m.menuitemid inner join "
//					+ constants.Table_Price_Toppings
//					+ " p on p.menuitemid=m.menuitemid and p.typeid=c.price_typeid and c.IsOffer=0 where isToppings=0 order by c.date";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				DecimalFormat decimalFormat = new DecimalFormat("00.00");				
//				for (int i = 0; i < cr.getCount(); i++) {
//					cr.moveToPosition(i);					
//					if (result != "") {
//						result = result + "," + "{\"itemname\":\""
//								+ cr.getString(1)
//								+ "\",\"price\":{\"price\":\""
//								+ Float.parseFloat(decimalFormat.format(cr.getDouble(2))) + "\",\"type\":\""
//								+ cr.getString(6) + "\",\"typeid\":"
//								+ cr.getInt(3) + "},\"qty\":" + cr.getInt(4)
//								+ ",\"orderitemId\":" + cr.getInt(0)
//								+ " ##TOPPING##}";
//					} else {
//						result = "{\"itemname\":\"" + cr.getString(1)
//								+ "\",\"price\":{\"price\":\""
//								+ Float.parseFloat(decimalFormat.format(cr.getDouble(2))) + "\",\"type\":\""
//								+ cr.getString(6) + "\",\"typeid\":"
//								+ cr.getInt(3) + "},\"qty\":" + cr.getInt(4)
//								+ ",\"orderitemId\":" + cr.getInt(0)
//								+ " ##TOPPING##}";
//					}
//
//					String sql1 = "select pt.menuitemid,pt.typeid,pt.price,pt.type from "
//							+ constants.Table_CartTopping
//							+ " c inner join "
//							+ constants.Table_Price_Toppings
//							+ " pt on c.menuitemid=pt.menuitemid and c.top_typeid=pt.typeid where c.menuitemid="
//							+ cr.getInt(0)
//							+ " and c.cartid="
//							+ cr.getInt(7)
//							+ " and pt.isToppings=1 order by pt.type";
//					Cursor cr1 = myDataBase.rawQuery(sql1, null);
//					if (cr1 != null && cr1.getCount() > 0) {
//						String strToppingsList = "";
//						for (int j = 0; j < cr1.getCount(); j++) {
//							cr1.moveToPosition(j);
//							if (strToppingsList != "") {
//								strToppingsList = strToppingsList
//										+ ",{\"price\":\"" + Float.parseFloat(decimalFormat.format(cr1.getDouble(2)))
//										+ "\",\"type\":\"" + cr1.getString(3)
//										+ "\",\"typeid\": " + cr1.getInt(1)
//										+ "}";
//							} else {
//								strToppingsList = "{\"price\":\""
//										+ Float.parseFloat(decimalFormat.format(cr1.getDouble(2))) + "\",\"type\":\""
//										+ cr1.getString(3) + "\",\"typeid\": "
//										+ cr1.getInt(1) + "}";
//							}
//						}
//						result = result.replace("##TOPPING##",
//								",\"ToppingsList\":[" + strToppingsList + "]");
//					} else {
//						result = result.replace("##TOPPING##",
//								",\"ToppingsList\":[]");
//					}
//					cr1.close();
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return result;
//	}
//
//	public void DeleteCart(int cartid) {
//		openDatabase();
//		try {
//			if (cartid > 0) {
//				myDataBase.delete(constants.Table_Cart, " cartId=" + cartid
//						+ "", null);
//				myDataBase.delete(constants.Table_CartTopping, " cartId="
//						+ cartid + "", null);
//				myDataBase.delete(constants.Table_CartOffer, " cartId="
//						+ cartid + "", null);
//			} else {
//				myDataBase.delete(constants.Table_Cart, null, null);
//				myDataBase.delete(constants.Table_CartTopping, null, null);
//				myDataBase.delete(constants.Table_CartOffer, null, null);
//			}
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public float RetriveCartTotal() {
//		float result = 0;
//		// TODO Auto-generated method stub
//		openDatabase();
//		try {
//			// sql = "select menuitemid,price,quantity,cartid from "+
//			// constants.Table_Cart +" order by date";
//			String sql = "select c.menuitemid,p.price,c.quantity,c.cartid from "
//					+ constants.Table_Cart
//					+ " c "
//					+ " inner join "
//					+ constants.Table_Menuitem
//					+ " m on c.menuitemid=m.menuitemid "
//					+ " inner join "
//					+ constants.Table_Price_Toppings
//					+ " p on p.menuitemid=m.menuitemid and p.typeid=c.price_typeid "
//					+ " where isToppings=0 and c.IsOffer=0 order by c.date";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				for (int i = 0; i < cr.getCount(); i++) {
//					cr.moveToPosition(i);
//					float resultTop = 0;
//					String sql1 = "select pt.price from "
//							+ constants.Table_CartTopping
//							+ " c inner join "
//							+ constants.Table_Price_Toppings
//							+ " pt on c.menuitemid=pt.menuitemid and c.top_typeid=pt.typeid where c.menuitemid="
//							+ cr.getInt(0) + " and c.cartid=" + cr.getInt(3)
//							+ " and pt.isToppings=1 order by pt.type";
//					Cursor cr1 = myDataBase.rawQuery(sql1, null);
//					if (cr1 != null && cr1.getCount() > 0) {
//						for (int j = 0; j < cr1.getCount(); j++) {
//							cr1.moveToPosition(j);
//							resultTop = resultTop + cr1.getFloat(0);
//						}
//					}
//					cr1.close();
//					result = result
//							+ ((cr.getFloat(1) + resultTop) * cr.getInt(2));
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return result;
//	}
//
//	public void InsertLikeDislike(JSONObject objLikeDislike) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("menuItemId", objLikeDislike.getInt("menuItemId"));
//			cv.put("customerId", objLikeDislike.getInt("customerId"));
//			cv.put("likes", objLikeDislike.getInt("likes"));
//			cv.put("dislikes", objLikeDislike.getInt("dislikes"));
//			cv.put("date", objLikeDislike.getString("date"));
//			cv.put("issend", objLikeDislike.getInt("issend"));
//			cv.put("locationid", objLikeDislike.getInt("locationid"));
//			cv.put("isFacebookShare", objLikeDislike.getInt("isFacebookShare"));
//			long id;
//			id = myDataBase.insert(constants.Table_LikeDislike, null, cv);
//			Log.i("insert LikeDislike : ", String.valueOf(id));
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public boolean ExistLikeDislike(JSONObject objLikeDislike, int status) {
//		boolean result = false;
//		openDatabase();
//		try {
//
//			String sql = "select count(*) as cnt from "
//					+ constants.Table_LikeDislike + " where menuItemId="
//					+ objLikeDislike.getInt("menuItemId") + " "
//					+ " and customerId=" + objLikeDislike.getInt("customerId")
//					+ " and locationid=" + objLikeDislike.getInt("locationid")
//					+ " " + " and date='" + objLikeDislike.getString("date")
//					+ "'";
//			if (status == 1) {
//				sql = sql + " and likes=1";
//			} else if (status == 0) {
//				sql = sql + " and dislikes=1";
//			}
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				if (cr.getInt(0) > 0) {
//					result = true;
//				}
//			}
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return result;
//	}
//	public void RemoveDislike(JSONObject objLikeDislike) {
//		// boolean result=false;
//		openDatabase();
//		try {
//
//			String sql = "select count(*) as cnt from "
//					+ constants.Table_LikeDislike + " where menuItemId="
//					+ objLikeDislike.getInt("menuItemId") + " "
//					+ " and customerId=" + objLikeDislike.getInt("customerId")
//					+ " and locationid=" + objLikeDislike.getInt("locationid")
//					+ " " + " and likes=1";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				if (cr.getInt(0) > 0) {
//					myDataBase.delete(
//							constants.Table_LikeDislike,
//							" menuItemId="
//									+ objLikeDislike.getInt("menuItemId")
//									+ " and customerId="
//									+ objLikeDislike.getInt("customerId")
//									+ " and locationid="
//									+ objLikeDislike.getInt("locationid")
//									+ " and likes=1", null);
//					// result = true;
//				}
//			}
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		// return result;
//	}
//	public void UpdateMenuItem(int menuitemid, boolean type) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("menuitemId", menuitemid);
//			String strType = "";
//
//			int totalLikesDislike = 0;
//			if (type == true) {
//				strType = " totallikes ";
//			} else {
//				strType = " totaldislikes ";
//			}
//
//			String sql = "select " + strType + " from "
//					+ constants.Table_Menuitem + " where menuItemId="
//					+ menuitemid + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				totalLikesDislike = cr.getInt(0);
//			}
//			if (type == true) {
//				cv.put("totallikes", totalLikesDislike + 1);
//			} else {
//				cv.put("totaldislikes", totalLikesDislike + 1);
//			}
//			myDataBase.update(constants.Table_Menuitem, cv, " menuItemId="
//					+ menuitemid + "", null);
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public ArrayList<clsMenuItem> RetriveFavorites(int custId, String orderby) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsMenuItem> arrlist = new ArrayList<clsMenuItem>();
//		String orderbystring = "menuItemName";
//		try {
//			if (orderby == "N") {
//				orderbystring = "menuItemName";
//			} else if (orderby == "L") {
//				orderbystring = "totallikes desc";
//			}
//
//			String sql = "select m.menuitemId,m.description,m.menuItemName,m.imgurl,m.thumbimgurl,m.totaldislikes,m.totallikes,m.catId from "
//					+ constants.Table_Menuitem
//					+ " m inner join "
//					+ constants.Table_LikeDislike
//					+ " l on m.menuitemId=l.menuitemId "
//					+ " where l.customerId="
//					+ custId
//					+ " and l.likes=1 order by " + orderbystring + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsMenuItem objMenu = new clsMenuItem();
//					objMenu.RowId = i;
//					objMenu.MenuItemId = cr.getInt(0);
//					objMenu.CategoryId = cr.getInt(7);
//					objMenu.Description = cr.getString(1);
//					objMenu.MenuItemName = cr.getString(2);
//					objMenu.ImgURL = cr.getString(3);
//					objMenu.ThumbimgURL = cr.getString(4);
//					objMenu.Totaldislikes = cr.getInt(5);
//					objMenu.Totallikes = cr.getInt(6);
//					arrlist.add(objMenu);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//	public ArrayList<clsLocationTime> RetriveLocationTime(int locId) {
//		// NOTE : function to insert or update location time.
//		ArrayList<clsLocationTime> resultListloc = null;
//		openDatabase();
//		try {
//			String sql = "select nameday,openingtime,openingtimemeridian,closingtime,closingtimemeridian,weekname from "
//					+ constants.Table_LocationTime
//					+ " where locationid="
//					+ locId + " order by nameday";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				resultListloc = new ArrayList<clsLocationTime>();
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsLocationTime objloc = new clsLocationTime();
//					objloc.NameDay = cr.getInt(0);
//					objloc.OpeningTime = cr.getString(1);
//					objloc.OpeningTimeMeridian = cr.getString(2);
//					objloc.ClosingTime = cr.getString(3);
//					objloc.ClosingTimeMeridian = cr.getString(4);
//					objloc.WeekName = cr.getString(5);
//					resultListloc.add(objloc);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			Log.i("Exe : ", e.getMessage());
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return resultListloc;
//	}
//	public clsRestaurant GetRestaurant(int restaurantId) {
//		openDatabase();
//		clsRestaurant objRest = null;
//		try {
//			Cursor c = myDataBase
//					.rawQuery(
//							"select restaurantId,ownerId,restaurantName,logourl,AboutUs,updateddate,currencycode,currencysymbol,hasmultipleprice,iswaiting,isdelivery,istakeaway,cultureName,ISDCode,facebook,twitter,linkedin from "
//									+ constants.Table_Restaurant
//									+ " where restaurantId= " + restaurantId,
//							null);
//			c.moveToFirst();
//			if (c != null && c.getCount() > 0) {
//				objRest = new clsRestaurant();
//				objRest.RestaurantId = c.getInt(0);
//				objRest.OwnerId = c.getInt(1);
//				objRest.RestaurantName = c.getString(2);
//				objRest.Logourl = c.getString(3);
//				objRest.AboutUs = c.getString(4);
//				objRest.Updateddate = c.getString(5);
//				objRest.Currencycode = c.getString(6);
//				objRest.Currencysymbol = c.getString(7);
//				objRest.Hasmultipleprice = Boolean.valueOf(c.getString(8));
//				if(c.getInt(9)==1)
//				{objRest.Iswaiting = true;}
//				else{objRest.Iswaiting = false;}
//				
//				if(c.getInt(10)==1)
//				{objRest.Isdelivery = true;}
//				else{objRest.Isdelivery = false;}
//				
//				if(c.getInt(11)==1)
//				{objRest.Istakeaway = true;}
//				else{objRest.Istakeaway = false;}				
//				
//				objRest.CultureName = c.getString(12);
//				objRest.ISDCode = c.getString(13);
//				objRest.Facebook = c.getString(14);
//				objRest.Twitter = c.getString(15);
//				objRest.Linkedin = c.getString(16);
//			}
//			c.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//
//		return objRest;
//	}
//
//	public String GetISDCode(int restaurantId) {
//		openDatabase();
//		String result = null;
//		try {
//			Cursor c = myDataBase.rawQuery("select ISDCode from "
//					+ constants.Table_Restaurant + " where restaurantId= "
//					+ restaurantId, null);
//			c.moveToFirst();
//			if (c != null && c.getCount() > 0) {
//				result = c.getString(0);
//			}
//			c.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//
//		return result;
//	}
//
//	public ArrayList<clsLocation> RetriveLocationsHome() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsLocation> arrlist = null;
//		try {
//			String sql = "select LocationId,Location_name from "
//					+ constants.Table_Location + " order by Location_name";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				arrlist = new ArrayList<clsLocation>();
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsLocation objLoc = new clsLocation();
//					objLoc.LocationId = cr.getInt(0);
//					objLoc.Location_name = cr.getString(1);
//					objLoc.Address = "";
//					objLoc.Country = "";
//					objLoc.State = "";
//					objLoc.City = "";
//					objLoc.Latitude = "";
//					objLoc.Longitude = "";
//					objLoc.Phone = "";
//					objLoc.Mobile = "";
//					objLoc.Fax = "";
//					objLoc.Zipcode = "";
//					objLoc.Email = "";
//					objLoc.bookingtimelimit = 0;
//					arrlist.add(objLoc);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//
//		return arrlist;
//	}
//	public ArrayList<clsLocation> RetriveAllLocations() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsLocation> arrlist = null;
//		try {
//			String sql = "select LocationId,Location_name,Address,Country,State,City,Latitude,Longitude,Phone,Mobile,Fax,Zipcode,Email,bookingtimelimit from "
//					+ constants.Table_Location + " order by Location_name";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				arrlist = new ArrayList<clsLocation>();
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsLocation objLoc = new clsLocation();
//					objLoc = new clsLocation();
//					objLoc.LocationId = cr.getInt(0);
//					objLoc.Location_name = cr.getString(1);
//					objLoc.Address = cr.getString(2);
//					objLoc.Country = cr.getString(3);
//					objLoc.State = cr.getString(4);
//					objLoc.City = cr.getString(5);
//					objLoc.Latitude = cr.getString(6);
//					objLoc.Longitude = cr.getString(7);
//					objLoc.Phone = cr.getString(8);
//					objLoc.Mobile = cr.getString(9);
//					objLoc.Fax = cr.getString(10);
//					objLoc.Zipcode = cr.getString(11);
//					objLoc.Email = cr.getString(12);
//					objLoc.bookingtimelimit = cr.getInt(13);
//					arrlist.add(objLoc);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//
//		return arrlist;
//	}
//	public clsLocation RetriveLocationsById(int locid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsLocation objLoc = null;
//		try {
//			String strQuery = "", strTop = " LIMIT 1 ";
//			if (locid > 0) {
//				strQuery = " where LocationId = " + locid + "";
//				strTop = "";
//			}
//			String sql = "select LocationId,Location_name,Address,Country,State,City,Latitude,Longitude,Phone,Mobile,Fax,Zipcode,Email,bookingtimelimit from "
//					+ constants.Table_Location
//					+ strQuery
//					+ " order by Location_name" + strTop;
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				objLoc = new clsLocation();
//				objLoc.LocationId = cr.getInt(0);
//				objLoc.Location_name = cr.getString(1);
//				objLoc.Address = cr.getString(2);
//				objLoc.Country = cr.getString(3);
//				objLoc.State = cr.getString(4);
//				objLoc.City = cr.getString(5);
//				objLoc.Latitude = cr.getString(6);
//				objLoc.Longitude = cr.getString(7);
//				objLoc.Phone = cr.getString(8);
//				objLoc.Mobile = cr.getString(9);
//				objLoc.Fax = cr.getString(10);
//				objLoc.Zipcode = cr.getString(11);
//				objLoc.Email = cr.getString(12);
//				objLoc.bookingtimelimit = cr.getInt(13);
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objLoc;
//	}
//
//	public int RetriveNextLocationId(int locid, String flg) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		int resultLocId = 0;
//		try {
//			String sql = "";
//			if (flg == "N") {
//				sql = "select LocationId from " + constants.Table_Location
//						+ " where LocationId > " + locid
//						+ " order by Location_name LIMIT 1";
//			} else if (flg == "P") {
//				sql = "select LocationId from " + constants.Table_Location
//						+ " where LocationId < " + locid
//						+ " order by Location_name LIMIT 1";
//			}
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				resultLocId = cr.getInt(0);
//			}
//			cr.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return resultLocId;
//	}
//
//	public List<clsLocation> RetriveLocations() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		List<clsLocation> arrlist = new ArrayList<clsLocation>();
//		try {
//			String sql = "select LocationId,Location_name,Address,Country,State,City,Latitude,Longitude,Phone,Mobile,Fax,Zipcode,Email,bookingtimelimit from "
//					+ constants.Table_Location + " order by Location_name";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsLocation objLoc = new clsLocation();
//					objLoc.LocationId = cr.getInt(0);
//					objLoc.Location_name = cr.getString(1);
//					objLoc.Address = cr.getString(2);
//					objLoc.Country = cr.getString(3);
//					objLoc.State = cr.getString(4);
//					objLoc.City = cr.getString(5);
//					objLoc.Latitude = cr.getString(6);
//					objLoc.Longitude = cr.getString(7);
//					objLoc.Phone = cr.getString(8);
//					objLoc.Mobile = cr.getString(9);
//					objLoc.Fax = cr.getString(10);
//					objLoc.Zipcode = cr.getString(11);
//					objLoc.Email = cr.getString(12);
//					objLoc.bookingtimelimit = cr.getInt(13);
//					arrlist.add(objLoc);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//
//		return arrlist;
//	}
//
//	public ArrayList<clsCategory> RetriveCategory(int parentcatid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsCategory> arrlist = new ArrayList<clsCategory>();
//		try {
//			String sql = "select cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category
//					+ " where cat_parent_id="
//					+ parentcatid + " order by row_id";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsCategory objCat = new clsCategory();
//					objCat.RowId = i;
//					objCat.CategoryId = cr.getInt(0);
//					objCat.CategoryTitle = cr.getString(1);
//					objCat.CategoryParentId = cr.getInt(2);
//					objCat.CategoryDescription = cr.getString(3);
//					if (Integer.parseInt(cr.getString(4)) == 1) {
//						objCat.IsLast = true;
//					} else {
//						objCat.IsLast = false;
//					}
//					arrlist.add(objCat);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public clsCategory RetriveCategoryById(int catid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select row_id,cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category + " where cat_id=" + catid + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToPosition(0);
//				objCat = new clsCategory();
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(1);
//				objCat.CategoryTitle = cr.getString(2);
//				objCat.CategoryParentId = cr.getInt(3);
//				objCat.CategoryDescription = cr.getString(4);
//				if (Integer.parseInt(cr.getString(5)) == 1) {
//					objCat.IsLast = true;
//				} else {
//					objCat.IsLast = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public clsCategory RetriveNextCategoryById(int catid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select row_id,cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category
//					+ " where cat_id in (select cat_id from "
//					+ constants.Table_Category
//					+ " where cat_id > "
//					+ catid
//					+ " and cat_parent_id in (select cat_parent_id from "
//					+ constants.Table_Category
//					+ " where cat_id="
//					+ catid
//					+ ") order by row_id LIMIT 1) ";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objCat = new clsCategory();
//				cr.moveToPosition(0);
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(1);
//				objCat.CategoryTitle = cr.getString(2);
//				objCat.CategoryParentId = cr.getInt(3);
//				objCat.CategoryDescription = cr.getString(4);
//				if (Integer.parseInt(cr.getString(5)) == 1) {
//					objCat.IsLast = true;
//				} else {
//					objCat.IsLast = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public clsCategory RetriveNNextCategoryById(int catid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select row_id,cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category
//					+ " where cat_id in (select cat_id from "
//					+ constants.Table_Category
//					+ " where cat_parent_id="
//					+ catid + " order by row_id LIMIT 1) ";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objCat = new clsCategory();
//				cr.moveToPosition(0);
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(1);
//				objCat.CategoryTitle = cr.getString(2);
//				objCat.CategoryParentId = cr.getInt(3);
//				objCat.CategoryDescription = cr.getString(4);
//				if (Integer.parseInt(cr.getString(5)) == 1) {
//					objCat.IsLast = true;
//				} else {
//					objCat.IsLast = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public clsCategory RetrivePrevCategoryById(int catid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select row_id,cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category
//					+ " where cat_id in (select cat_id from "
//					+ constants.Table_Category
//					+ " where cat_id < "
//					+ catid
//					+ " and cat_parent_id in (select cat_parent_id from "
//					+ constants.Table_Category
//					+ " where cat_id="
//					+ catid
//					+ ") order by row_id desc LIMIT 1) ";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objCat = new clsCategory();
//				cr.moveToPosition(0);
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(1);
//				objCat.CategoryTitle = cr.getString(2);
//				objCat.CategoryParentId = cr.getInt(3);
//				objCat.CategoryDescription = cr.getString(4);
//				if (Integer.parseInt(cr.getString(5)) == 1) {
//					objCat.IsLast = true;
//				} else {
//					objCat.IsLast = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public clsCategory RetrivePPrevCategoryById(int catid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select row_id,cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category
//					+ " where cat_id in (select cat_id from "
//					+ constants.Table_Category
//					+ " where cat_parent_id="
//					+ catid + " order by row_id desc LIMIT 1) ";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objCat = new clsCategory();
//				cr.moveToPosition(0);
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(1);
//				objCat.CategoryTitle = cr.getString(2);
//				objCat.CategoryParentId = cr.getInt(3);
//				objCat.CategoryDescription = cr.getString(4);
//				if (Integer.parseInt(cr.getString(5)) == 1) {
//					objCat.IsLast = true;
//				} else {
//					objCat.IsLast = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public clsCategory RetriveCategoryByParentId(int catid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select row_id,cat_id,cat_name,cat_parent_id,description,isLast from "
//					+ constants.Table_Category
//					+ " where cat_id in (select cat_parent_id from "
//					+ constants.Table_Category + " where cat_id=" + catid + ")";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objCat = new clsCategory();
//				cr.moveToPosition(0);
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(1);
//				objCat.CategoryTitle = cr.getString(2);
//				objCat.CategoryParentId = cr.getInt(3);
//				objCat.CategoryDescription = cr.getString(4);
//				if (Integer.parseInt(cr.getString(5)) == 1) {
//					objCat.IsLast = true;
//				} else {
//					objCat.IsLast = false;
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public clsCategory RetriveCategoryByItemId(int itemid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsCategory objCat = null;
//		try {
//			String sql = "select c.cat_id,c.cat_name from "
//					+ constants.Table_Category + " c inner join "
//					+ constants.Table_Menuitem
//					+ " i on c.cat_id=i.catid where i.menuitemId =" + itemid
//					+ "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objCat = new clsCategory();
//				cr.moveToPosition(0);
//				objCat.RowId = cr.getInt(0);
//				objCat.CategoryId = cr.getInt(0);
//				objCat.CategoryTitle = cr.getString(1);
//				objCat.CategoryParentId = 0;
//				objCat.CategoryDescription = "";
//				objCat.IsLast = false;
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objCat;
//	}
//
//	public ArrayList<clsMenuItem> RetriveMenuItem(int catid, String orderby, int custId) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsMenuItem> arrlist = new ArrayList<clsMenuItem>();
//		String orderbystring = "menuItemName";
//		try {
//			if (orderby == "N") {
//				orderbystring = "menuItemName";
//			} else if (orderby == "L") {
//				orderbystring = "totallikes desc";
//			}
//
//			String sql = "select menuitemId,description,menuItemName,imgurl,thumbimgurl,totaldislikes,totallikes from "
//					+ constants.Table_Menuitem
//					+ " where catId="
//					+ catid
//					+ " order by " + orderbystring + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsMenuItem objMenu = new clsMenuItem();
//					objMenu.RowId = i;
//					objMenu.MenuItemId = cr.getInt(0);
//					objMenu.CategoryId = catid;
//					objMenu.Description = cr.getString(1);
//					objMenu.MenuItemName = cr.getString(2);
//					objMenu.ImgURL = cr.getString(3);
//					objMenu.ThumbimgURL = cr.getString(4);
//					objMenu.Totaldislikes = cr.getInt(5);
//					objMenu.Totallikes = cr.getInt(6);
//					try{
//					Cursor cursor = myDataBase.rawQuery("select menuItemId from "+constants.Table_LikeDislike+" where l.customerId="
//							+ custId
//							+ " and l.likes=1 and menuItemId="+objMenu.MenuItemId, null);
//							if(cursor.getCount()>0){
//								objMenu.isMyFav=true;
//							}else{
//								objMenu.isMyFav=false;
//							}
//							cursor.close();
//					}catch(Exception e){
//						e.printStackTrace();
//					}
//					arrlist.add(objMenu);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public clsMenuItem RetriveMenuItemDtls(int itemid) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsMenuItem objmenuitem = null;
//		try {
//			String sql = "select menuitemId,catid,description,menuItemName,imgurl,thumbimgurl,totaldislikes,totallikes from "
//					+ constants.Table_Menuitem + " where menuitemId=" + itemid;
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				objmenuitem = new clsMenuItem();
//				cr.moveToFirst();
//				objmenuitem = new clsMenuItem();
//				objmenuitem.RowId = 0;
//				objmenuitem.MenuItemId = cr.getInt(0);
//				objmenuitem.CategoryId = cr.getInt(1);
//				objmenuitem.Description = cr.getString(2);
//				objmenuitem.MenuItemName = cr.getString(3);
//				objmenuitem.ImgURL = cr.getString(4);
//				objmenuitem.ThumbimgURL = cr.getString(5);
//				objmenuitem.Totaldislikes = cr.getInt(6);
//				objmenuitem.Totallikes = cr.getInt(7);
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objmenuitem;
//	}
//
//	public ArrayList<clsPriceToppings> RetrivePrice(int menuItemId,
//			int locationId) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsPriceToppings> arrlist = new ArrayList<clsPriceToppings>();
//		try {
//			String sql = "select price,type,typeid,isToppings,LocationId,rowid from "
//					+ constants.Table_Price_Toppings
//					+ " where isToppings=0 and menuitemId="
//					+ menuItemId
//					+ " and (locationid="+ locationId +" or locationid=0) order by price";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsPriceToppings objPrice = new clsPriceToppings();
//					objPrice.price = cr.getFloat(0);
//					objPrice.type = cr.getString(1);
//					objPrice.typeid = cr.getInt(2);
//					objPrice.isToppings = Boolean.parseBoolean(cr.getString(3));
//					objPrice.LocationId = cr.getInt(4);
//					objPrice.type = cr.getString(1);
//					objPrice.rowId = cr.getInt(5);
//					arrlist.add(objPrice);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public ArrayList<clsPriceToppings> RetriveToppingPrice(int menuItemId,
//			int locationId) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsPriceToppings> arrlist = new ArrayList<clsPriceToppings>();
//		try {
//			String sql = "select price,type,typeid,isToppings,LocationId,rowid from "
//					+ constants.Table_Price_Toppings
//					+ " where isToppings=1 and menuitemId="
//					+ menuItemId
//					+ " and (locationid="+ locationId +" or locationid=0) order by price";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsPriceToppings objPrice = new clsPriceToppings();
//					objPrice.price = cr.getFloat(0);
//					objPrice.type = cr.getString(1);
//					objPrice.typeid = cr.getInt(2);
//					objPrice.isToppings = Boolean.parseBoolean(cr.getString(3));
//					objPrice.LocationId = cr.getInt(4);
//					objPrice.type = cr.getString(1);
//					objPrice.rowId = cr.getInt(5);
//					arrlist.add(objPrice);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public clsPriceToppings RetrivePriceForListing(int menuItemId,
//			int locationId) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsPriceToppings objPrice = null;
//		try {
//			String sql = "select price,type,typeid,isToppings,LocationId,rowid from "
//					+ constants.Table_Price_Toppings
//					+ " where menuitemId="
//					+ menuItemId
//					+ " and (locationid="+ locationId +" or locationid=0) "
//					+ " order by price";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				cr.moveToFirst();
//				objPrice = new clsPriceToppings();
//				objPrice.price = cr.getFloat(0);
//				objPrice.type = cr.getString(1);
//				objPrice.typeid = cr.getInt(2);
//				objPrice.isToppings = Boolean.parseBoolean(cr.getString(3));
//				objPrice.LocationId = cr.getInt(4);
//				objPrice.type = cr.getString(1);
//				objPrice.count = n;
//				objPrice.rowId = cr.getInt(5);
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objPrice;
//	}
//
//	public ArrayList<clsStatus> RetriveStatus(int menuItemId) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		ArrayList<clsStatus> arrlist = new ArrayList<clsStatus>();
//		try {
//			String sql = "select rowid,menuitemId,title,iconurl from "
//					+ constants.Table_Status + " where menuitemId="
//					+ menuItemId + " order by rowid";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				int n = cr.getCount();
//				Log.i("n", String.valueOf(n));
//				for (int i = 0; i < n; i++) {
//					cr.moveToPosition(i);
//					clsStatus objStatus = new clsStatus();
//					objStatus.MenuitemId = cr.getInt(1);
//					objStatus.Title = cr.getString(2);
//					objStatus.Iconurl = cr.getString(3);
//					arrlist.add(objStatus);
//				}
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return arrlist;
//	}
//
//	public void DeleteLocation(int locationId) {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Location, "LocationId = "
//					+ locationId, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void InsertAlertMessages(clsAlertMessage clsaltmsg) {
//		openDatabase();
//		try {
//			ContentValues cv = new ContentValues();
//			cv.put("messageId", clsaltmsg.MessageId);
//			cv.put("title", clsaltmsg.Title);
//			cv.put("message", clsaltmsg.Message);
//
//			Cursor c = myDataBase.rawQuery("select * from "
//					+ constants.Table_Alert_Message + " where messageId= "
//					+ clsaltmsg.MessageId, null);
//			long id;
//			if (c != null && c.getCount() > 0) {
//				id = myDataBase.update(constants.Table_Alert_Message, cv,
//						"messageId = " + clsaltmsg.MessageId, null);
//				Log.i("update Alert Message : ", String.valueOf(id));
//			} else {
//				id = myDataBase.insert(constants.Table_Alert_Message, null, cv);
//				Log.i("insert Alert Message : ", String.valueOf(id));
//			}
//			c.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public void DeleteAlertMessages(int messageId) {
//		openDatabase();
//		try {
//			myDataBase.delete(constants.Table_Alert_Message, "messageId = "
//					+ messageId, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//
//	public clsAlertMessage RetriveMessage(String key) {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsAlertMessage objMsg = null;
//		try {
//			String sql = "select messageId,title,message from "
//					+ constants.Table_Alert_Message + " where title='" + key
//					+ "'";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				objMsg = new clsAlertMessage();
//				objMsg.MessageId = cr.getInt(0);
//				objMsg.Title = cr.getString(1);
//				objMsg.Message = cr.getString(2);
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objMsg;
//	}
//
//	public clsUser RetriveCustomer() {
//		// TODO Auto-generated method stub
//		openDatabase();
//		clsUser objUser = null;
//		try {
//			String sql = "select customerId,firstname,lastname,deliveryaddress,totalrewardpoints,emailid,mobile,phone,fax,isGuest,password,isActive from "
//					+ constants.Table_User + "";
//			Cursor cr = myDataBase.rawQuery(sql, null);
//			if (cr != null && cr.getCount() > 0) {
//				cr.moveToFirst();
//				objUser = new clsUser();
//				objUser.customerId = cr.getInt(0);
//				objUser.firstname = cr.getString(1);
//				objUser.lastname = cr.getString(2);
//				objUser.deliveryaddress = cr.getString(3);
//				objUser.totalrewardpoints = cr.getInt(4);
//				objUser.emailid = cr.getString(5);
//				objUser.mobile = cr.getString(6);
//				objUser.phone = cr.getString(7);
//				objUser.fax = cr.getString(8);
//			}
//			cr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//		return objUser;
//	}
//	
//	public void clearCartData()
//	{
//		openDatabase();
//		myDataBase.delete(constants.Table_Cart,null,null);
//		myDataBase.delete(constants.Table_CartOffer,null,null);
//		myDataBase.delete(constants.Table_CartTopping,null,null);
//		
//		//myDataBase.execSQL("TRUNCATE table" + constants.Table_Cart);
//		//myDataBase.execSQL("TRUNCATE table" + constants.Table_CartOffer);
//		//myDataBase.execSQL("TRUNCATE table" + constants.Table_CartTopping);
//		
//		closeDataBase();
//		SQLiteDatabase.releaseMemory();
//	}
//}