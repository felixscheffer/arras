// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.github.fscheffer.arras.demo.pages;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.github.fscheffer.arras.demo.EditorContent;
import org.github.fscheffer.arras.services.SubmissionProcessor;

@Import(module = "mediumEditorDemo")
public class MediumEditorDemo {

    private static final String SUBMIT = "arrasSubmit";

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

    @Inject
    private ComponentResources  resources;

    @Inject
    private SubmissionProcessor processor;

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

    @OnEvent(value = SUBMIT)
    void onSubmit(@RequestParameter("content") String content) {

        JSONObject object = new JSONObject(content);

        this.processor.process(object);

        this.alertManager.success("Saved changes!");
    }

    public Link getUrl() {
        return this.resources.createEventLink(MediumEditorDemo.SUBMIT);
    }
}
