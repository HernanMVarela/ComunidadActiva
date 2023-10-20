package frgp.utn.edu.ar.controllers.data.model;

public class ReseniaReporte {
    private Reporte reporte;
    private Usuario votante;
    private float puntaje;

    public ReseniaReporte(Reporte reporte, Usuario votante, float puntaje) {
        this.reporte = reporte;
        this.votante = votante;
        this.puntaje = puntaje;
    }

    public ReseniaReporte() {
    }

    @Override
    public String toString() {
        return "ReseniaReporte{" +
                "reporte=" + reporte +
                ", votante=" + votante +
                ", puntaje=" + puntaje +
                '}';
    }

    public Reporte getReporte() {
        return reporte;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

    public Usuario getVotante() {
        return votante;
    }

    public void setVotante(Usuario votante) {
        this.votante = votante;
    }

    public float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(float puntaje) {
        this.puntaje = puntaje;
    }
}
