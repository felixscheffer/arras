package com.github.fscheffer.arras.components;

import java.util.Collection;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.Renderable;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.internal.util.SelectModelRenderer;

import com.github.fscheffer.arras.ArrasConstants;

@SupportsInformalParameters
@Import(module = "arras/select2", stylesheet = ArrasConstants.SELECT2_CSS_PATH_VALUE)
public class MultiSelect2 extends AbstractField {

    /**
     * A ValueEncoder used to convert server-side objects (provided from the
     * "source" parameter) into unique client-side strings (typically IDs) and
     * back. Note: this component does NOT support ValueEncoders configured to
     * be provided automatically by Tapestry.
     */
    @Parameter(required = true, allowNull = false)
    private ValueEncoder<Object>   encoder;

    /**
     * Model used to define the values and labels used when rendering.
     */
    @Parameter(required = true, allowNull = false)
    private SelectModel            model;

    /**
     * The list of selected values from the {@link org.apache.tapestry5.SelectModel}. This will be updated when the form
     * is submitted. If the value for the parameter is null, a new list will be created, otherwise the existing list
     * will be cleared. If unbound, defaults to a property of the container matching this component's id.
     * <p/>
     * Prior to Tapestry 5.4, this allowed null, and a list would be created when the form was submitted. Starting
     * with 5.4, the selected list may not be null, and it need not be a list (it may be, for example, a set).
     */
    @Parameter(required = true, autoconnect = true, allowNull = false)
    private Collection<Object>     selected;

    /**
     * The object that will perform input validation. The validate binding prefix is generally used to provide
     * this object in a declarative fashion.
     *
     * @since 5.2.0
     */
    @Parameter(defaultPrefix = BindingConstants.VALIDATE)
    @SuppressWarnings("unchecked")
    private FieldValidator<Object> validate;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String                 placeholder;

    public final Renderable        mainRenderer = new Renderable() {

        @Override
        public void render(MarkupWriter writer) {
            SelectModelRenderer visitor = new SelectModelRenderer(
                                                                  writer,
                                                                  MultiSelect2.this.encoder,
                                                                  false) {

                @Override
                protected boolean isOptionSelected(OptionModel optionModel,
                                                   String clientValue) {

                    return MultiSelect2.this.selected.contains(optionModel.getValue());
                }

            };

            MultiSelect2.this.model.visit(visitor);
        }
    };

    @Override
    protected void processSubmission(String controlName) {

        String[] parameterValues = this.request.getParameters(controlName);

        if (parameterValues == null) {
            parameterValues = new String[] {};
        }

        // Use a couple of local variables to cut down on access via bindings

        Collection<Object> selected = this.selected;

        selected.clear();

        ValueEncoder encoder = this.encoder;

        // TODO: Validation error if the model does not contain a value.

        for (String value : parameterValues) {

            Object objectValue = encoder.toValue(value);

            selected.add(objectValue);
        }

        putPropertyNameIntoBeanValidationContext("selected");

        try {
            this.fieldValidationSupport.validate(selected, this.resources, this.validate);

            this.selected = selected;
        }
        catch (final ValidationException e) {
            this.validationTracker.recordError(this, e.getMessage());
        }

        removePropertyNameFromBeanValidationContext();
    }

    void beginRender(MarkupWriter writer) {
        writer.element("select", "name", getControlName(), "multiple", "multiple", "disabled", getDisabledValue(),
                       "data-component-type", "select2", "class", "form-control");

        if (this.placeholder != null) {
            writer.attributes("data-placeholder", this.placeholder);
        }

        this.resources.renderInformalParameters(writer);
    }

    void afterRender(MarkupWriter writer) {
        writer.end();
    }

    public String getDisabledValue() {
        return this.disabled ? "disabled" : null;
    }

    /**
     * Computes a default value for the "validate" parameter using
     * {@link org.apache.tapestry5.services.FieldValidatorDefaultSource}.
     */
    Binding defaultValidate() {
        return this.defaultProvider.defaultValidatorBinding("selected", this.resources);
    }

}
