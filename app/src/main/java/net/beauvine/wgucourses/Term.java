package net.beauvine.wgucourses;

/**
 * Created by beau on 6/3/16.
 */
public class Term {
    private long id;
    private String term;
    private String termStart;
    private String termEnd;

    public void setId(long id) {
        this.id = id;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public long getId() {

        return id;
    }

    public String getTerm() {
        return term;
    }

    public String getTermStart() {
        return termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    @Override
    public String toString() {
        return term;
    }
}
