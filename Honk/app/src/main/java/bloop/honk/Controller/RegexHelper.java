package bloop.honk.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {
    public RegexHelper() {
    }

    public boolean alphanumericRegex(String input) {
        Pattern alphanumericSpace = Pattern.compile("^[a-zA-Z0-9]{6,12}$"); //only allow alphanumeric
        Matcher matcher = alphanumericSpace.matcher(input);
        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean emailRegex(String input) {
        Pattern courseCode = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); //email format
        Matcher matcher = courseCode.matcher(input);
        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }
}
