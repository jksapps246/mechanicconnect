package com.jksapps.mechanicconnect

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper2(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(myDB: SQLiteDatabase) {
        //create users table
        myDB.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " (" +
                USERS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_USERNAME_COl + " TEXT , "+
                USERS_PASSWORD_COL + " TEXT, " +
                USERS_REMEMBER_COL + " INTEGER, " +
                USERS_MECHANIC_COL + " INTEGER)")
        //create accounts table

        myDB.execSQL("CREATE TABLE " + ACCOUNT_TABLE_NAME + " (" +
                USERS_ID_COL + " INTEGER PRIMARY KEY, "+
                ACCOUNT_FIRST_NAME_COL + " TEXT, " +
                ACCOUNT_LAST_NAME_COL + " TEXT, " +
                ACCOUNT_EMAIL_COL + " TEXT, " +
                ACCOUNT_ADDRESS1_COL + " TEXT, " +
                ACCOUNT_ADDRESS2_COL + " TEXT, " +
                ACCOUNT_CITY_COL + " TEXT, " +
                ACCOUNT_POSTAL_CODE_COL + " TEXT, " +
                ACCOUNT_PHONE_NUMBER_COL + " INTEGER)")

        myDB.execSQL("CREATE TABLE " + PAYMENT_TABLE_NAME + " (" +
                USERS_ID_COL + " INTEGER PRIMARY KEY, "+
                PAYMENT_FULL_NAME_BILLING_COL + " TEXT, " +
                PAYMENT_ADDRESS_BILLING1_COL + " TEXT, " +
                PAYMENT_ADDRESS_BILLING2_COL + " TEXT, " +
                PAYMENT_CITY_COL + " TEXT, " +
                PAYMENT_POSTAL_CODE_BILLING_COL + " TEXT, " +
                PAYMENT_CARD_NUMBER_COL + " TEXT, " +
                PAYMENT_CARD_NUMBER_DATE_COL + " TEXT, " +
                PAYMENT_CARD_NUMBER_CODE_COL + " TEXT)")

        //create notify table
        myDB.execSQL("CREATE TABLE " + NOTIFY_TABLE_NAME + " (" +
                NOTIFY_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_ID_COL + " INTEGER , " +
                NOTIFY_TYPE_COl + " TEXT , " +
                NOTIFY_DESCRIPTION_COL + " TEXT, " +
                NOTIFY_IMAGE_COL + " TEXT, " +
                NOTIFY_DATE_COL + " TEXT, " +
                NOTIFY_ACTION_COL + " TEXT)")

        //create email table
        myDB.execSQL("CREATE TABLE " + EMAIL_TABLE_NAME + " (" +
                USERS_ID_COL + " INTEGER PRIMARY KEY, " +
                EMAIL_CODE_COl + " INTEGER)")

        //create mechanic table
        myDB.execSQL("CREATE TABLE " + MECHANIC_TABLE_NAME + " (" +
                USERS_ID_COL + " INTEGER PRIMARY KEY, " +
                MECHANIC_ID_COL + " INTEGER , " +
                MECHANIC_ACCEPTED_COL+" INTEGER)")

        //create chat table
        myDB.execSQL("CREATE TABLE " + CHAT_TABLE_NAME + " (" +
                CHAT_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CHAT_SENDER_ID_COL + " INTEGER, " +
                CHAT_RECEIVER_ID_COL + " INTEGER, " +
                CHAT_MESSAGE_COL + " TEXT, " +
                CHAT_TIMESTAMP_COL+" TEXT)")

        //create service table
        myDB.execSQL("CREATE TABLE " + SERVICE_TABLE_NAME + " (" +
                SERVICE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MECHANIC_ID_COL + " INTEGER, " +
                SERVICE_IMAGE_COL + " INTEGER, " +
                SERVICE_NAME_COL + " TEXT, " +
                SERVICE_TYPE_COL + " TEXT)")

        //create vehicle table
        myDB.execSQL("CREATE TABLE " + VEHICLE_TABLE_NAME + " (" +
                USERS_ID_COL + " INTEGER PRIMARY KEY, " +
                VEHICLE_TYPE_COL + " TEXT, " +
                VEHICLE_NAME_COL + " TEXT, " +
                VEHICLE_MODEL_COL + " TEXT, " +
                VEHICLE_IMAGE_COL+" BLOB)")

    }

    override fun onUpgrade(myDB: SQLiteDatabase, i: Int, i1: Int) {
        myDB.execSQL("DROP TABLE IF EXISTS $USERS_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $ACCOUNT_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $PAYMENT_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $NOTIFY_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $EMAIL_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $MECHANIC_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $CHAT_TABLE_NAME")
        myDB.execSQL("DROP TABLE IF EXISTS $VEHICLE_TABLE_NAME")
        onCreate(myDB)
    }
///////////////////////////////////////////////////////////////////////////
//User table functions
///////////////////////////////////////////////////////////////////////////
    fun insertUserData(username: String?, password: String?, rememberMe: Int?, mechanic: Int?): Boolean {
        var isOK = true
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USERS_USERNAME_COl, username)
        contentValues.put(USERS_PASSWORD_COL, password)
        contentValues.put(USERS_REMEMBER_COL, rememberMe)
        contentValues.put(USERS_MECHANIC_COL, mechanic)
        val result = myDB.insert(USERS_TABLE_NAME, null, contentValues)
        isOK = result != -1L
        myDB.close()
        return isOK
    }//insertUserData()
    fun checkUsername(username: String): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val cursor = myDB.rawQuery("Select * from $USERS_TABLE_NAME where $USERS_USERNAME_COl = ?", arrayOf(username))
        isOK = cursor.count > 0
        myDB.close()
        return isOK
    }//checkUsername()

    fun getUsername(userId: String): String {
        var isOk = ""
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select $USERS_USERNAME_COl from $USERS_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        if(cursor != null) {
            cursor!!.moveToFirst()
            val col = cursor.getColumnIndex(USERS_USERNAME_COl)
            isOk = cursor.getString(col)
        }
        myDB.close()
        return isOk
    }//checkUsername()
    fun checkUsernameMechanic(userId: String?): Boolean {
        var isOK = false
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $USERS_TABLE_NAME where $USERS_MECHANIC_COL = 1 and $USERS_ID_COL = ?", arrayOf(userId))
        isOK = cursor.count > 0
        myDB.close()
        return isOK
    }//checkUsernameMechanic()
    fun checkUsernamePassword(username: String, password: String): Int {
        var userId = -1
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery(
            "Select * from $USERS_TABLE_NAME where $USERS_USERNAME_COl = ? and $USERS_PASSWORD_COL = ?",
            arrayOf(username, password)
        )
        if(cursor != null && cursor.count > 0) {
            cursor!!.moveToFirst()
            val col = cursor.getColumnIndex(USERS_ID_COL)
            userId = cursor.getInt(col)
            myDB.close()
        }
        //return userId
        return userId
    }//checkUsernamePassword()
    fun checkAutoLogin(): Cursor? {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $USERS_TABLE_NAME where $USERS_REMEMBER_COL = 1", null)
        if(cursor == null)
            myDB.close()
        return cursor
    }//checkAutoLogin()
    fun updateRemember(username: String): Boolean {
        var isOK = true
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        //clear all other remembers
        //clearRemember()
        //update new user with remember
        contentValues.put(USERS_REMEMBER_COL, 1)
        val result = myDB.update(USERS_TABLE_NAME,contentValues, "$USERS_USERNAME_COl = ?", arrayOf(username))
        isOK = result > 0
        myDB.close()
        return isOK
    }//updateRemember()
    fun clearRemember() {
        try {
            val myDB = this.writableDatabase
            val contentValues = ContentValues()
            //clear all other remembers
            contentValues.put(USERS_REMEMBER_COL, 0)
            myDB.update(USERS_TABLE_NAME, contentValues, "$USERS_REMEMBER_COL = 1", null)
            myDB.close()
        }
        catch(e: Exception) {
            //Toast.makeText(this, e.message,Toast.LENGTH_LONG).show()
        }
    }//clearRemember()

///////////////////////////////////////////////////////////////////////////
//Account table functions
///////////////////////////////////////////////////////////////////////////
    fun insertAccountData(user_id: Int?, first_name: String?, last_name: String?, email: String?, address1: String?, address2: String?,city: String?, postal_code: String?, phone_number: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val cursor = myDB.rawQuery("Select * from $ACCOUNT_TABLE_NAME where $USERS_ID_COL = ?",arrayOf(user_id.toString()))
        val contentValues = ContentValues()
        contentValues.put(USERS_ID_COL, user_id)
        contentValues.put(ACCOUNT_FIRST_NAME_COL, first_name)
        contentValues.put(ACCOUNT_LAST_NAME_COL, last_name)
        contentValues.put(ACCOUNT_EMAIL_COL, email)
        contentValues.put(ACCOUNT_ADDRESS1_COL, address1)
        contentValues.put(ACCOUNT_ADDRESS2_COL, address2)
        contentValues.put(ACCOUNT_CITY_COL, city)
        contentValues.put(ACCOUNT_POSTAL_CODE_COL, postal_code)
        contentValues.put(ACCOUNT_PHONE_NUMBER_COL, phone_number)
        if(cursor != null && cursor.count > 0) {// userId exists and updated columns
            val result = myDB.update(ACCOUNT_TABLE_NAME,contentValues, "$USERS_ID_COL = ?", arrayOf(user_id.toString()))
            isOK = result != -1
        }
        else {//userId doesn't add new columns
            val result = myDB.insert(ACCOUNT_TABLE_NAME, null, contentValues)
            isOK = result != -1L
        }
        myDB.close()
        return isOK
    }//insertAccountData()
    fun checkAccountDetails(userId: String?): Boolean{
        var isOK = true
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $ACCOUNT_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        isOK = cursor.count > 0
        myDB.close()
        return isOK
    }//checkAccountDetails()
    fun getAccountDetails(userId: String?): ContentValues {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $ACCOUNT_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()
        if(cursor.count > 0) {
            cursor!!.moveToFirst()
            var col = cursor.getColumnIndex(USERS_ID_COL)
            contentValues.put(USERS_ID_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_FIRST_NAME_COL)
            contentValues.put(ACCOUNT_FIRST_NAME_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_LAST_NAME_COL)
            contentValues.put(ACCOUNT_LAST_NAME_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_EMAIL_COL)
            contentValues.put(ACCOUNT_EMAIL_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_ADDRESS1_COL)
            contentValues.put(ACCOUNT_ADDRESS1_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_ADDRESS2_COL)
            contentValues.put(ACCOUNT_ADDRESS2_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_CITY_COL)
            contentValues.put(ACCOUNT_CITY_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_POSTAL_CODE_COL)
            contentValues.put(ACCOUNT_POSTAL_CODE_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_PHONE_NUMBER_COL)
            contentValues.put(ACCOUNT_PHONE_NUMBER_COL, cursor.getString(col))
        }
        myDB.close()
        return contentValues
    }//getAccountDetails()
    fun getContactDetails(userId: String?): ContentValues {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $ACCOUNT_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()
        if(cursor.count > 0) {
            cursor!!.moveToFirst()
            var col = cursor.getColumnIndex(ACCOUNT_FIRST_NAME_COL)
            contentValues.put(ACCOUNT_FIRST_NAME_COL, cursor.getString(col))

            col = cursor.getColumnIndex(ACCOUNT_EMAIL_COL)
            contentValues.put(ACCOUNT_EMAIL_COL, cursor.getString(col))
        }
        myDB.close()
        return contentValues
    }//getContactDetails()
///////////////////////////////////////////////////////////////////////////
//Payment table functions
///////////////////////////////////////////////////////////////////////////
    fun insertPaymentData(user_id: Int?, full_name_billing: String?, address_billing1: String?, address_billing2: String?, city: String?, postal_code_billing: String?, card_number: String?,card_number_date: String?, card_number_code: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val cursor = myDB.rawQuery("Select * from $PAYMENT_TABLE_NAME where $USERS_ID_COL = ?",arrayOf(user_id.toString()))
        val contentValues = ContentValues()
        contentValues.put(USERS_ID_COL, user_id)
        contentValues.put(PAYMENT_FULL_NAME_BILLING_COL, full_name_billing)
        contentValues.put(PAYMENT_ADDRESS_BILLING1_COL, address_billing1)
        contentValues.put(PAYMENT_ADDRESS_BILLING2_COL, address_billing2)
        contentValues.put(PAYMENT_POSTAL_CODE_BILLING_COL, postal_code_billing)
        contentValues.put(PAYMENT_CITY_COL, city)
        contentValues.put(PAYMENT_CARD_NUMBER_COL, card_number)
        contentValues.put(PAYMENT_CARD_NUMBER_DATE_COL, card_number_date)
        contentValues.put(PAYMENT_CARD_NUMBER_CODE_COL, card_number_code)

        if(cursor != null && cursor.count > 0) {// userId exists and updated columns
            val result = myDB.update(PAYMENT_TABLE_NAME,contentValues, "$USERS_ID_COL = ?", arrayOf(user_id.toString()))
            isOK = result != -1
        }
        else {//userId doesn't add new columns
            val result = myDB.insert(PAYMENT_TABLE_NAME, null, contentValues)
            isOK = result != -1L
        }
        myDB.close()
        return isOK

    }//insertPaymentData()
    fun checkPaymentDetails(userId: String?): Boolean{
        var isOK = true
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $PAYMENT_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        isOK = cursor.count > 0
        myDB.close()
        return isOK
    }//checkPaymentDetails()
    fun getPaymentDetails(userId: String?): ContentValues {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $PAYMENT_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()
        if(cursor.count > 0) {
            cursor!!.moveToFirst()
            var col = cursor.getColumnIndex(USERS_ID_COL)
            contentValues.put(USERS_ID_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_FULL_NAME_BILLING_COL)
            contentValues.put(PAYMENT_FULL_NAME_BILLING_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_ADDRESS_BILLING1_COL)
            contentValues.put(PAYMENT_ADDRESS_BILLING1_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_ADDRESS_BILLING2_COL)
            contentValues.put(PAYMENT_ADDRESS_BILLING2_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_POSTAL_CODE_BILLING_COL)
            contentValues.put(PAYMENT_POSTAL_CODE_BILLING_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_CITY_COL)
            contentValues.put(PAYMENT_CITY_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_CARD_NUMBER_COL)
            contentValues.put(PAYMENT_CARD_NUMBER_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_CARD_NUMBER_DATE_COL)
            contentValues.put(PAYMENT_CARD_NUMBER_DATE_COL, cursor.getString(col))

            col = cursor.getColumnIndex(PAYMENT_CARD_NUMBER_CODE_COL)
            contentValues.put(PAYMENT_CARD_NUMBER_CODE_COL, cursor.getString(col))
        }
        myDB.close()
        return contentValues
    }//getPaymentDetails()
///////////////////////////////////////////////////////////////////////////
//NOTIFY table functions
///////////////////////////////////////////////////////////////////////////
    fun checkNotify(userId: String?): Cursor? {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $NOTIFY_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        if(cursor == null)
            myDB.close()
        return cursor
    }//checkNotify()
    fun deleteNotify(userId: String?, type: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val result = myDB.delete(NOTIFY_TABLE_NAME,"$USERS_ID_COL = ? AND $NOTIFY_TYPE_COl = ? ",arrayOf(userId,type))
        isOK = result > 0
        myDB.close()
        return isOK
    }//deleteNotify()
    fun insertNotify(userId: Int?, type: String?, desc: String?, image: String?, date: String?, action: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USERS_ID_COL, userId)
        contentValues.put(NOTIFY_TYPE_COl, type)
        contentValues.put(NOTIFY_DESCRIPTION_COL, desc)
        contentValues.put(NOTIFY_IMAGE_COL, image)
        contentValues.put(NOTIFY_DATE_COL, date)
        contentValues.put(NOTIFY_ACTION_COL, action)
        val result = myDB.insert(NOTIFY_TABLE_NAME,null,contentValues)
        isOK = result > 0
        myDB.close()
        return isOK
    }//insertNotify()
///////////////////////////////////////////////////////////////////////////
//Email table functions
///////////////////////////////////////////////////////////////////////////
    fun getEmailValidation(userId: String?) : Int {
        var code = -1
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $EMAIL_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()
        if(cursor.count > 0) {
            cursor!!.moveToFirst()
            var col = cursor.getColumnIndex(EMAIL_CODE_COl)
            code = cursor.getInt(col)
        }
        myDB.close()
        return code
    }
    fun deleteEmailValidation(userId: String?) : Boolean{
        var isOK = false
        val myDB = this.writableDatabase
        val result = myDB.delete(EMAIL_TABLE_NAME,"$USERS_ID_COL = ?",arrayOf(userId))
        isOK = result > 0
        myDB.close()
        return isOK
    }
    fun insertEmailValidation(userId: Int?, code: Int?) : Boolean{
        var isOK = false
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USERS_ID_COL, userId)
        contentValues.put(EMAIL_CODE_COl, code)
        val result = myDB.insert(EMAIL_TABLE_NAME,null,contentValues)
        isOK = result > 0
        myDB.close()
        return isOK
    }

///////////////////////////////////////////////////////////////////////////
//Mechanic Map functions
///////////////////////////////////////////////////////////////////////////
    fun getUserAddress(userId: String?): ContentValues {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $ACCOUNT_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()
        var name : String
        var address : String
        if(cursor.count > 0) {
            cursor!!.moveToFirst()
            //store name
            var col = cursor.getColumnIndex(ACCOUNT_FIRST_NAME_COL)
            name = cursor.getString(col)
            col = cursor.getColumnIndex(ACCOUNT_LAST_NAME_COL)
            name += " " + cursor.getString(col)
            contentValues.put("name", name)

            //store address
            col = cursor.getColumnIndex(ACCOUNT_ADDRESS1_COL)
            address = cursor.getString(col)
            col = cursor.getColumnIndex(ACCOUNT_ADDRESS2_COL)
            address += ", " + cursor.getString(col)
            col = cursor.getColumnIndex(ACCOUNT_CITY_COL)
            address += ", " + cursor.getString(col)
            col = cursor.getColumnIndex(ACCOUNT_POSTAL_CODE_COL)
            address += ", " + cursor.getString(col)
            contentValues.put("address", address)
        }
        myDB.close()
        return contentValues
    }//getUserAddress()

    fun getMechanics(): Cursor {
        val myDB = this.readableDatabase
        val sql = "Select $USERS_TABLE_NAME.$USERS_ID_COL as id, $USERS_USERNAME_COl as business_name, $ACCOUNT_FIRST_NAME_COL || \" \" ||$ACCOUNT_LAST_NAME_COL as name, " +
                "$ACCOUNT_ADDRESS1_COL||\", \"||$ACCOUNT_ADDRESS2_COL||\", \"||$ACCOUNT_CITY_COL||\", \"||$ACCOUNT_POSTAL_CODE_COL as address "+
                "from $ACCOUNT_TABLE_NAME inner join $USERS_TABLE_NAME on $USERS_TABLE_NAME.$USERS_ID_COL = $ACCOUNT_TABLE_NAME.$USERS_ID_COL "+
                "where $USERS_MECHANIC_COL = 1"
        val cursor = myDB.rawQuery(sql, null)
        if(cursor == null)
            myDB.close()
        return cursor
    }//getMechanics()
    fun getMechanicDetails(mechanicId: String): Cursor {
        val myDB = this.readableDatabase
        val sql = "Select $USERS_TABLE_NAME.$USERS_ID_COL as id, $USERS_USERNAME_COl as business_name, $ACCOUNT_FIRST_NAME_COL || \" \" ||$ACCOUNT_LAST_NAME_COL as name, " +
                "$ACCOUNT_ADDRESS1_COL||\", \"||$ACCOUNT_ADDRESS2_COL||\", \"||$ACCOUNT_CITY_COL||\", \"||$ACCOUNT_POSTAL_CODE_COL as address "+
                "from $ACCOUNT_TABLE_NAME inner join $USERS_TABLE_NAME on $USERS_TABLE_NAME.$USERS_ID_COL = $ACCOUNT_TABLE_NAME.$USERS_ID_COL "+
                "where $USERS_MECHANIC_COL = 1 and $USERS_TABLE_NAME.$USERS_ID_COL = ?"
        val cursor = myDB.rawQuery(sql, arrayOf(mechanicId))
        if(cursor == null)
            myDB.close()
        return cursor
    }//getMechanics()
///////////////////////////////////////////////////////////////////////////
//Mechanic User functions
///////////////////////////////////////////////////////////////////////////
fun getMechanicUser(userId: String?): Int {
    var isOk = 0
    val myDB = this.readableDatabase
    val cursor = myDB.rawQuery("Select $MECHANIC_ID_COL from $MECHANIC_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
    if(cursor != null) {
        val col = cursor.getColumnIndex(MECHANIC_ID_COL)
        isOk = cursor.getInt(col)
    }
    myDB.close()
    return isOk
}//getMechanicUser()
    fun getMechanicUserAccepted(userId: String?): Int {
        var isOk = 0
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select $MECHANIC_ID_COL from $MECHANIC_TABLE_NAME where $MECHANIC_ACCEPTED_COL = 1 and $USERS_ID_COL = ?", arrayOf(userId))
        if(cursor != null && cursor.count > 0) {
            cursor!!.moveToFirst()
            val col = cursor.getColumnIndex(MECHANIC_ID_COL)
            isOk = cursor.getInt(col)
        }
        myDB.close()
        return isOk
    }//checkMechanicUser()
    fun getUsersMechanicToAccept(userId: String?): Cursor {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select $USERS_TABLE_NAME.$USERS_ID_COL,$USERS_USERNAME_COl from $MECHANIC_TABLE_NAME inner join $USERS_TABLE_NAME " +
                " on $USERS_TABLE_NAME.$USERS_ID_COL = $MECHANIC_TABLE_NAME.$USERS_ID_COL where $MECHANIC_ACCEPTED_COL = 0 and $MECHANIC_ID_COL = ? ", arrayOf(userId))
        if(cursor == null)
            myDB.close()
        return cursor
    }//getUserMechanicToAccept()

    fun getUserMechanic(userId: String?): Int {
        var isOk = 0
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select $USERS_ID_COL from $MECHANIC_TABLE_NAME where $MECHANIC_ID_COL = ? ", arrayOf(userId))
        if(cursor != null && cursor.count > 0) {
            cursor!!.moveToFirst()
            val col = cursor.getColumnIndex(MECHANIC_ID_COL)
            isOk = cursor.getInt(col)
        }
        myDB.close()
        return isOk
    }//getUserMechanic()
fun checkMechanicUser(userId: String?): Boolean {
    var isOk = false
    val myDB = this.readableDatabase
    val cursor = myDB.rawQuery("Select * from $MECHANIC_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
    if(cursor != null)
        isOk = cursor.count > 0
    myDB.close()
    return isOk
}//checkMechanicUser()

    fun deleteMechanicUser(userId: String?, mechanicId: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val result = myDB.delete(MECHANIC_TABLE_NAME,"$USERS_ID_COL = ? AND $MECHANIC_ID_COL = ? ",arrayOf(userId,mechanicId))
        isOK = result > 0
        myDB.close()
        return isOK
    }//deleteNotify()

    fun acceptMechanicUser(userId: String?, mechanicId: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MECHANIC_ACCEPTED_COL, 1)
        val result = myDB.update(MECHANIC_TABLE_NAME,contentValues,"$USERS_ID_COL = ? AND $MECHANIC_ID_COL = ? ",arrayOf(userId,mechanicId))
        isOK = result > 0
        myDB.close()
        return isOK
    }//acceptMechanicUser()

    fun insertMechanicUser(userId: Int?, mechanicId: Int?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MECHANIC_ID_COL, mechanicId)
        contentValues.put(USERS_ID_COL, userId)
        contentValues.put(MECHANIC_ACCEPTED_COL, 0)

        val result = myDB.insert(MECHANIC_TABLE_NAME,null,contentValues)
        isOK = result > 0
        myDB.close()
        return isOK
    }//insertMechanicUser()
///////////////////////////////////////////////////////////////////////////
//Chat User functions
///////////////////////////////////////////////////////////////////////////
    fun insertMessage(senderId: String, receiverId: String, message: String): Boolean{
        var isOK = false
        val timeStamp = System.currentTimeMillis()
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CHAT_SENDER_ID_COL, senderId.toInt())
        contentValues.put(CHAT_RECEIVER_ID_COL, receiverId.toInt())
        contentValues.put(CHAT_MESSAGE_COL, message)
        contentValues.put(CHAT_TIMESTAMP_COL, timeStamp.toString())
        val result = myDB.insert(CHAT_TABLE_NAME,null,contentValues)
        isOK = result > 0
        myDB.close()
        return isOK
    }//insertMessage()
///////////////////////////////////////////////////////////////////////////
//Vehicle functions
///////////////////////////////////////////////////////////////////////////
    fun getServices(mechanicId: String?, type: String?): Cursor? {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $SERVICE_TABLE_NAME where $MECHANIC_ID_COL = ? and $SERVICE_TYPE_COL = ?", arrayOf(mechanicId,type))
        if(cursor == null)
            myDB.close()
        return cursor
    }//getServices()
    fun deleteService(mechanicId: String?, name: String?): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val result = myDB.delete(SERVICE_TABLE_NAME,"$MECHANIC_ID_COL = ? and $SERVICE_NAME_COL = ? ",arrayOf(mechanicId,name))
        isOK = result > 0
        myDB.close()
        return isOK
    }//deleteService()
    fun insertService(userId: Int?, name: String?, Image: Int?,type: String): Boolean {
        var isOK = false
        val myDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MECHANIC_ID_COL, userId)
        contentValues.put(SERVICE_IMAGE_COL, Image)
        contentValues.put(SERVICE_NAME_COL, name)
        contentValues.put(SERVICE_TYPE_COL, type)
        val result = myDB.insert(SERVICE_TABLE_NAME,null,contentValues)
        isOK = result > 0
        myDB.close()
        return isOK
    }//insertService()



///////////////////////////////////////////////////////////////////////////
//Vehicle functions
///////////////////////////////////////////////////////////////////////////

    fun insertVehicle(userId: String, vType: String, vName: String, vModel: String, vImage: ByteArray): Boolean{
        var isOK = false
        val myDB = this.writableDatabase
        val cursor = myDB.rawQuery("Select * from $VEHICLE_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()
        contentValues.put(USERS_ID_COL, userId.toInt())
        contentValues.put(VEHICLE_TYPE_COL, vType)
        contentValues.put(VEHICLE_NAME_COL, vName)
        contentValues.put(VEHICLE_MODEL_COL, vModel)
        contentValues.put(VEHICLE_IMAGE_COL, vImage)
        if(cursor != null && cursor.count > 0) {// userId exists and updated columns

            val result = myDB.update(
                VEHICLE_TABLE_NAME,
                contentValues,
                "$USERS_ID_COL = ?",
                arrayOf(userId)
            )
            isOK = result != -1
        }else {//userId doesn't add new columns
            val result = myDB.insert(VEHICLE_TABLE_NAME, null, contentValues)
            isOK = result != -1L
        }
        myDB.close()
        return isOK
    }
    fun getUserVehicle(userId: String): ContentValues {
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $VEHICLE_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))
        val contentValues = ContentValues()

        if(cursor != null && cursor.count > 0) {
            cursor!!.moveToFirst()
            //store name
            var col = cursor.getColumnIndex(VEHICLE_TYPE_COL)
            val vType = cursor.getString(col)
            col = cursor.getColumnIndex(VEHICLE_NAME_COL)
            val vName = cursor.getString(col)
            col = cursor.getColumnIndex(VEHICLE_MODEL_COL)
            val vModel = cursor.getString(col)
            col = cursor.getColumnIndex(VEHICLE_IMAGE_COL)
            val vImage = cursor.getBlob(col)

            contentValues.put(VEHICLE_TYPE_COL, vType)
            contentValues.put(VEHICLE_NAME_COL, vName)
            contentValues.put(VEHICLE_MODEL_COL, vModel)
            contentValues.put(VEHICLE_IMAGE_COL, vImage)
        }
        myDB.close()
        return contentValues
    }//getUserAddress()

    fun checkUserVehicle(userId: String): Boolean {
        var isOk = false
        val myDB = this.readableDatabase
        val cursor = myDB.rawQuery("Select * from $VEHICLE_TABLE_NAME where $USERS_ID_COL = ?", arrayOf(userId))

        if(cursor != null && cursor.count > 0) {
            isOk = true
        }
        myDB.close()
        return isOk
    }//getUserAddress()

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private const val DATABASE_NAME = "Login.db"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        // below is the variables for Users table
        const val USERS_TABLE_NAME = "users"
        const val USERS_ID_COL = "user_id"
        const val USERS_USERNAME_COl = "username"
        const val USERS_PASSWORD_COL = "password"
        const val USERS_REMEMBER_COL = "remember"
        const val USERS_MECHANIC_COL = "mechanic"

        // below are the variables for Account table
        const val ACCOUNT_TABLE_NAME = "accounts"
        const val ACCOUNT_FIRST_NAME_COL = "fname"
        const val ACCOUNT_LAST_NAME_COL = "lname"
        const val ACCOUNT_EMAIL_COL = "email"
        const val ACCOUNT_ADDRESS1_COL = "address1"
        const val ACCOUNT_ADDRESS2_COL = "address2"
        const val ACCOUNT_CITY_COL = "city"
        const val ACCOUNT_POSTAL_CODE_COL = "postal_code"
        const val ACCOUNT_PHONE_NUMBER_COL = "phone_number"

        // below are the variables for Payment table
        const val PAYMENT_TABLE_NAME = "payments"
        const val PAYMENT_FULL_NAME_BILLING_COL = "full_name_billing1"
        const val PAYMENT_ADDRESS_BILLING1_COL = "address_billing1"
        const val PAYMENT_ADDRESS_BILLING2_COL = "address_billing2"
        const val PAYMENT_POSTAL_CODE_BILLING_COL = "postal_code_billing"
        const val PAYMENT_CITY_COL = "city_billing"
        const val PAYMENT_CARD_NUMBER_COL = "card_number"
        const val PAYMENT_CARD_NUMBER_DATE_COL = "card_number_date"
        const val PAYMENT_CARD_NUMBER_CODE_COL = "card_number_code"

        // below are the variables for Notify table
        const val NOTIFY_TABLE_NAME = "notify"
        const val NOTIFY_ID_COL = "notify_id"
        const val NOTIFY_TYPE_COl = "type"
        const val NOTIFY_DESCRIPTION_COL = "description"
        const val NOTIFY_IMAGE_COL = "image"
        const val NOTIFY_DATE_COL = "date"
        const val NOTIFY_ACTION_COL = "action"

        // below are the variables for Email table
        const val EMAIL_TABLE_NAME = "email"
        const val EMAIL_CODE_COl = "code"

        // below are the variables for Mechanic table
        const val MECHANIC_TABLE_NAME = "mechanic"
        const val MECHANIC_ID_COL = "mechanic_id"
        const val MECHANIC_ACCEPTED_COL = "accepted"

        // below are the variables for Chat table
        const val CHAT_TABLE_NAME = "chat"
        const val CHAT_ID_COL = "chat_id"
        const val CHAT_SENDER_ID_COL = "sender_id"
        const val CHAT_RECEIVER_ID_COL = "receiver_id"
        const val CHAT_MESSAGE_COL = "message"
        const val CHAT_TIMESTAMP_COL = "time_stamp"

        // below are the variables for Service table
        const val SERVICE_TABLE_NAME = "service"
        const val SERVICE_ID_COL = "service_id"
        const val SERVICE_NAME_COL = "service_name"
        const val SERVICE_IMAGE_COL = "service_image"
        const val SERVICE_TYPE_COL = "service_type"

        // below are the variables for Vehicle table
        const val VEHICLE_TABLE_NAME = "vehicle"
        const val VEHICLE_TYPE_COL = "vehicle_type"
        const val VEHICLE_NAME_COL = "vehicle_name"
        const val VEHICLE_MODEL_COL = "vehicle_model"
        const val VEHICLE_IMAGE_COL = "vehicle_image"

    }//companion
}