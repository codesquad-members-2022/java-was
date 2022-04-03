package webserver.servlet;

public enum ServletStatus {
    WAITING,
    RESPONSING;

    public boolean isAvailable(){
        return this.equals(WAITING);
    }
}
