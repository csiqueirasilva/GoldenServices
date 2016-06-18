package br.uva.goldenservices.database.recibos;

/**
 * Created by csiqueira on 18/06/16.
 */
public class Recibo {

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static enum Tipo {
        PRESTADOR,
        CLIENTE
    }

    private String desc;
    private Tipo tipo;
    private Integer id;
}
