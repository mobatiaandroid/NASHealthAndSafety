package com.nas.healthandsafety.constants

import android.content.Context
import android.content.SharedPreferences


class PreferenceManager {
    companion object {
        private const val sharedPrefNas = "NAS_EVAC"
        fun setAccessToken(context: Context, accessToken: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("access_token", accessToken)
            editor.apply()
        }
        fun getAccessToken(context: Context?): String {
            val tokenValue: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            tokenValue = sharedPreferences.getString("access_token", "").toString()
            return tokenValue
        }
        fun setStaffName(context: Context, staffName: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("staff_name", staffName)
            editor.apply()
        }
        fun getStaffName(context: Context?): String {
            val staffName: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            staffName = sharedPreferences.getString("staff_name", "").toString()
            return staffName
        }
        fun setStaffID(context: Context, staffID: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("staff_id", staffID)
            editor.apply()
        }

        fun getStaffID(context: Context?): String {
            val staffID: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            staffID = sharedPreferences.getString("staff_id", "").toString()
            return staffID
        }

        fun setFCMID(context: Context, staffID: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("fcm_id", staffID)
            editor.apply()
        }

        fun getFCMID(context: Context?): String {
            val staffID: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            staffID = sharedPreferences.getString("fcm_id", "").toString()
            return staffID
        }

        fun setIsFireMarshall(context: Context, value: Boolean) {
            val prefs = context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean("fire_marshall", value)
            editor.apply()
        }

        fun getIsFireMarshall(context: Context): Boolean {
            val prefs = context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            return prefs.getBoolean("fire_marshall", false)
        }
        fun setClassID(context: Context, classID:String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("class_id", classID)
            editor.apply()
        }
        fun getClassID(context: Context?): String {
            val classID: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            classID = sharedPreferences.getString("class_id", "").toString()
            return classID
        }
        fun setClassName(context: Context, className: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("class_name", className)
            editor.apply()
        }
        fun getClassName(context: Context): String {
            val className: String
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            className = sharedPreferences.getString("class_name", "").toString()
            return className
        }
//        fun setAbsentList(context: Context, absentList: ArrayList<Lists>?) {
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            var absentList2: ArrayList<Lists>
//            absentList2 = absentList!!
//            val gson = Gson()
//            if (absentList2 == null) {
//                absentList2 = ArrayList()
//            }
//            val json = gson.toJson(absentList2)
//            editor.putString("absent_list", json)
//            editor.apply()
//        }
//        fun getAbsentList(context: Context): ArrayList<Lists> {
//            var absentList: ArrayList<Lists> = ArrayList()
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("absent_list", null)
//            Log.e("Absent",json.toString())
//            val type: Type = object : TypeToken<ArrayList<Lists?>?>() {}.type
//            absentList = gson.fromJson<Any>(json, type) as ArrayList<Lists>
//            if (absentList == null) {
//                absentList = ArrayList()
//            }
//            return absentList
//        }
//        fun setPresentList(context: Context, presentList:ArrayList<Lists>) {
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            val gson = Gson()
//            val json = gson.toJson(presentList)
//            editor.putString("present_list", json)
//            editor.apply()
//        }
//        fun getPresentList(context: Context): ArrayList<Lists> {
//            val presentList: ArrayList<Lists>
//            val sharedPreferences: SharedPreferences =
//                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("present_list", null)
//            val type: Type = object : TypeToken<ArrayList<Lists?>?>() {}.type
//            presentList = gson.fromJson<Any>(json, type) as ArrayList<Lists>
//            return presentList
//        }
//        fun setStudentList(context: Context, studentlist:ArrayList<Lists>) {
//            val sharedPreferences: SharedPreferences =
//                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            val gson = Gson()
//            val json = gson.toJson(studentlist)
//            editor.putString("student_list", json)
//            editor.apply()
//
//        }
//        fun getStudentList(context: Context): ArrayList<Lists> {
//            val studentList: ArrayList<Lists>
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("student_list", null)
//            val type: Type = object : TypeToken<ArrayList<Lists?>?>() {}.type
//            studentList = gson.fromJson<Any>(json, type) as ArrayList<Lists>
//            return studentList
//        }

//        fun setAssemblyPoints(context: Context, assemblyPointsList: ArrayList<com.nas.fireevacuation.activity.staff_home.model.assembly_points_model.Lists>) {
//            val sharedPreferences: SharedPreferences =
//                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            Log.e("Assembly Points1", assemblyPointsList.toString())
//            val gson = Gson()
//            val json = gson.toJson(assemblyPointsList)
//            editor.putString("assemblyPointsList", json)
//            editor.apply()
//        }
//        fun getAssemblyPoints(context: Context): ArrayList<com.nas.fireevacuation.activity.staff_home.model.assembly_points_model.Lists> {
//            val assemblyPointsList: ArrayList<com.nas.fireevacuation.activity.staff_home.model.assembly_points_model.Lists>
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("assemblyPointsList", null)
//            Log.e("Assembly Pref", json.toString())
//            val type: Type = object : TypeToken<ArrayList<Lists?>?>() {}.type
//            assemblyPointsList = gson.fromJson<Any>(json, type) as ArrayList<com.nas.fireevacuation.activity.staff_home.model.assembly_points_model.Lists>
//            return assemblyPointsList
//        }
        fun setAssemblyPoint(context: Context, assemblyPoint: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("assemblyPoint", assemblyPoint)
            editor.apply()
        }
        fun getAssemblyPoint(context: Context): String {
            val assemblyPoint: String
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            assemblyPoint = sharedPreferences.getString("assemblyPoint", "").toString()
            return assemblyPoint
        }
//        fun setNotFoundList(context: Context, absentList: ArrayList<EvacuationStudentModel>?) {
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            var absentList2: ArrayList<EvacuationStudentModel>
//            absentList2 = absentList!!
//            val gson = Gson()
//            if (absentList2 == null) {
//                absentList2 = ArrayList()
//            }
//            val json = gson.toJson(absentList2)
//            editor.putString("absent_list", json)
//            editor.apply()
//        }
//        fun getNotFoundList(context: Context): ArrayList<EvacuationStudentModel> {
//            var absentList: ArrayList<EvacuationStudentModel> = ArrayList()
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("absent_list", null)
//            Log.e("Absent",json.toString())
//            val type: Type = object : TypeToken<ArrayList<EvacuationStudentModel?>?>() {}.type
//            absentList = gson.fromJson<Any>(json, type) as ArrayList<EvacuationStudentModel>
//            if (absentList == null) {
//                absentList = ArrayList()
//            }
//            return absentList
//        }
//        fun setFoundList(context: Context, presentList:ArrayList<EvacuationStudentModel>) {
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            val gson = Gson()
//            val json = gson.toJson(presentList)
//            editor.putString("present_list", json)
//            editor.apply()
//        }
//        fun getFoundList(context: Context): ArrayList<EvacuationStudentModel> {
//            val presentList: ArrayList<EvacuationStudentModel>
//            val sharedPreferences: SharedPreferences =
//                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("present_list", null)
//            val type: Type = object : TypeToken<ArrayList<EvacuationStudentModel?>?>() {}.type
//            presentList = gson.fromJson<Any>(json, type) as ArrayList<EvacuationStudentModel>
//            return presentList
//        }
//        fun setEvacStudentList(context: Context, studentlist:ArrayList<EvacuationStudentModel>) {
//            val sharedPreferences: SharedPreferences =
//                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val editor: SharedPreferences.Editor = sharedPreferences.edit()
//            val gson = Gson()
//            val json = gson.toJson(studentlist)
//            editor.putString("student_list", json)
//            editor.apply()
//
//        }
//        fun getEvacStudentList(context: Context): ArrayList<EvacuationStudentModel> {
//            val studentList: ArrayList<EvacuationStudentModel>
//            val sharedPreferences: SharedPreferences =
//                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
//            val gson = Gson()
//            val json = sharedPreferences.getString("student_list", null)
//            val type: Type = object : TypeToken<ArrayList<EvacuationStudentModel?>?>() {}.type
//            studentList = gson.fromJson<Any>(json, type) as ArrayList<EvacuationStudentModel>
//            return studentList
//        }

        fun getSubject(context: Context?): String {
            val staffID: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            staffID = sharedPreferences.getString("subject", "").toString()
            return staffID
        }
        fun setSubject(context: Context, subject:String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("subject", subject)
            editor.apply()
        }

        fun setFireRef(context: Context, firebaseReference: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("firebaseReference", firebaseReference)
            editor.apply()
        }
        fun getFireRef(context: Context?): String {
            val firebaseReference: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            firebaseReference = sharedPreferences.getString("firebaseReference", "").toString()
            return firebaseReference
        }
        fun setScrollPos(context: Context, pos: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("pos", pos)
            editor.apply()
        }
        fun getScrollPos(context: Context?): String {
            val pos: String
            val sharedPreferences: SharedPreferences =
                context!!.getSharedPreferences(sharedPrefNas, Context.MODE_PRIVATE)
            pos = sharedPreferences.getString("pos", "").toString()
            return pos
        }
    }
}