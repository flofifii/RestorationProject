package exceptionPackage;

public class TitleException extends Exception{
    private String wrongTitle;
    public TitleException(String wrongTitle, String message){
        super(message);
        setWrongTitle(wrongTitle);
    }
    public void setWrongTitle(String wrongTitle) {
        this.wrongTitle = wrongTitle;
    }
}
