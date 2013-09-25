package com.bdjamealapp.debug;

import com.bdjamealapp.Utils;

import java.util.ArrayList;

public class ErrorManager {
    private static final ArrayList<MealAppError> error_List = new ArrayList<MealAppError>();

    public static void catchError(final Exception e) {
        MealAppError error = new MealAppError(e.toString(), e);
        error_List.add(error);
        StringBuffer buf = new StringBuffer(e.toString());
        Utils.Debug.log(buf.toString() + " : " + e.toString());
        for (StackTraceElement element : e.getStackTrace()) {
            Utils.Debug.log(buf.toString() + " -> " + element.toString());
        }
    }

    public static void catchAndThrowError(final Exception e) throws Exception {
        catchError(e);
        throw e;
    }

    private static class MealAppError {
        public MealAppError(final String msg, final Exception e) {
            this.msg = msg;
            this.e = e;
        }

        private String msg;
        private Exception e;

    }

}
