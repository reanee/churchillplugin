package com.github.manolo8.darkbot.gui.plugins;

import com.github.manolo8.darkbot.extensions.features.FeatureDefinition;
import com.github.manolo8.darkbot.extensions.features.decorators.InstructionProviderDecorator;
import com.github.manolo8.darkbot.gui.components.MainButton;
import eu.darkbot.api.extensions.Behavior;
import eu.darkbot.api.extensions.InstructionProvider;
import eu.darkbot.api.extensions.Module;
import eu.darkbot.api.extensions.Task;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FeatureTypeButton extends MainButton {

    private static final Map<Class<?>, String> FEATURE_TYPES = new LinkedHashMap<>();
    static {
        FEATURE_TYPES.put(Module.class, "Module");
        FEATURE_TYPES.put(Behavior.class, "Behaviour");
        FEATURE_TYPES.put(Task.class, "Task");
    }
    private enum InstructionStatus {
        NONE, UNSURE, YES
    }

    private final FeatureDefinition<?> feature;
    private final String description;
    private InstructionStatus instr;

    FeatureTypeButton(FeatureDefinition<?> feature) {
        super("");
        this.feature = feature;
        this.instr = InstructionProvider.class.isAssignableFrom(feature.getClazz()) ? InstructionStatus.UNSURE : InstructionStatus.NONE;

        description = FEATURE_TYPES.entrySet().stream()
                .filter(e -> e.getKey().isAssignableFrom(feature.getClazz()))
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(","));

        setText(description.isEmpty() ? "-" : description.substring(0, 1));

        updateStatus(feature);
        feature.addStatusUiListener(this::updateStatus);
    }

    private void updateStatus(FeatureDefinition<?> feature) {
        if (instr == InstructionStatus.UNSURE && feature.getInstance() != null) {
            InstructionProvider p = (InstructionProvider) feature.getInstance();
            instr = p.instructions() == null && p.instructionsComponent() == null ? InstructionStatus.NONE : InstructionStatus.YES;
        }
        setEnabled(instr == InstructionStatus.YES);
        setToolTipText(description +
                (instr == InstructionStatus.NONE ? "" :
                        " - " + (isEnabled() ? "Show instructions" : "Feature not loaded, info unavailable")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isEnabled() || instr != InstructionStatus.YES) return;
        InstructionProviderDecorator.showInstructions(feature.getInstance(), feature.getName());
    }

}
