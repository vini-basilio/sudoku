package br.com.dio.util.model;

public enum GameStatusEnum {
    NON_STARTED("não iniciado"),
    INCOMPLETE(
            "incompleto"),
    COMPLETED("completo");

    private String label;

    GameStatusEnum(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
