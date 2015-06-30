package ga.chschtsch.milonhapashut;

public class Word {

    int _id;
    String _translated;
    String _translated2;
    String _translation;
    String _part;

    public Word(){

    }

    public Word(int id, String translated, String translated2, String translation, String part) {
        this._id = id;
        this._translated = translated;
        this._translated2 = translated2;
        this._translation = translation;
        this._part = part;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getTranslated(){
        return this._translated;
    }

    public void setTranslated(String translated){
        this._translated = translated;
    }

    public String getTranslated2(){
        return this._translated2;
    }

    public void setTranslated2(String translated2){
        this._translated2 = translated2;
    }

    public String getTranslation(){
        return this._translation;
    }

    public void setTranslation(String translation){
        this._translation = translation;
    }

    public String getPart(){
        return this._part;
    }

    public void setPart(String part){
        this._part = part;
    }
}
