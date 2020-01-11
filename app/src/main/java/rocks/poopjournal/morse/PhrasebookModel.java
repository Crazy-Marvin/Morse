package rocks.poopjournal.morse;

public class PhrasebookModel {
    int id;
    public String text;
    public String morse;

    public PhrasebookModel(int id, String text, String morse) {
        this.id = id;
        this.text = text;
        this.morse = morse;
    }
}
