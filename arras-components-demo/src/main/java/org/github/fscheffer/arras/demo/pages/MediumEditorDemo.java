package org.github.fscheffer.arras.demo.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.github.fscheffer.arras.demo.EditorContent;

public class MediumEditorDemo {

    @Persist
    @Property
    private String              value1, value2, value3, value4, value5;

    @Persist
    @Property
    private List<EditorContent> rows;

    @Property
    private EditorContent       row;

    @Inject
    private AlertManager        alertManager;

    @SetupRender
    void setup() {

        if (this.rows == null) {
            onReset();
        }
    }

    void onReset() {

        this.value1 = "<p>My father’s family name being Pirrip, and my Christian name Philip, "
                      + "my infant tongue could make of both names nothing longer or more explicit"
                      + "than Pip. So, I called myself Pip, and came to be called Pip.</p>";
        this.value2 = "<p>Hello Felix!</p>";
        this.value3 = null;
        this.value4 = "<p>Hello World!</p>";
        this.value5 = "<p>Far far away, behind the word mountains, far from the countries <b>Vokalia and Consonantia</b>, there"
                      + " live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large"
                      + " language ocean.</p>"
                      + "<blockquote>A small river named Duden flows by their place and supplies it with the necessary"
                      + " regelialia.</blockquote>"
                      + "<p>It is a paradisematic country, in which roasted parts of sentences fly into your mouth."
                      + " Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic"
                      + " life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far"
                      + " World of Grammar.</p>";

        this.rows = CollectionFactory.newList();
        this.rows.add(new EditorContent("Row 1"));
        this.rows.add(new EditorContent("Row 2"));
        this.rows.add(new EditorContent("Row 3"));
        this.rows.add(new EditorContent("Row 4"));
    }

    @OnEvent(value = EventConstants.SUCCESS)
    void onSubmit() {

        this.alertManager.success("Saved changes!");
    }
}
