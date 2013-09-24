package com.bdjamealapp.debug;

import com.bdjamealapp.Utils;

import java.util.ArrayList;

public class ErrorManager {
    private static final ArrayList<MealAppError> error_List = new ArrayList<MealAppError>();

    public static void catchError(final String msg, final Exception e) {
        MealAppError error = new MealAppError(msg, e);
        error_List.add(error);
        Utils.Debug.log(msg + " " + e.toString());
        Utils.Debug.log(error_List.toString());
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
