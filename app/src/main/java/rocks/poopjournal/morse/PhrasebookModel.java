package rocks.poopjournal.morse;

public class PhrasebookModel {
    public String text;
    public String morse;
    int id;

    public PhrasebookModel(int id, String text, String morse) {
        this.id = id;
        this.text = text;
        this.morse = morse;
    }
}
