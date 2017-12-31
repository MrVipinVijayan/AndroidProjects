package otto_library_android.coderzheaven.com.ottolibraryandroiddemo;

/**
 * Created by vipinvijayan on 31/12/17.
 */

public class Events {

    public static class FragmentActivityMessage {

        private String message;

        public FragmentActivityMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ActivityFragmentMessage {

        private String message;

        public ActivityFragmentMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
