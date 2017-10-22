package thacks.muse;

public class currentUser {
    private static String firstName, lastName, email;
    public currentUser(String _firstName, String _lastName, String _email){
        firstName=_firstName;
        lastName=_lastName;
        email=_email;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getEmail(){
        return email;
    }
}
