package ly.bithive.sugar;

public class Chat {

    private String content;
    private boolean isMine;

    public Chat(String content, boolean isMine) {
        this.content = content;
        this.isMine = isMine;
    }

    public String getContent() {
        return content;
    }

    public boolean isMine() {
        return isMine;
    }
}