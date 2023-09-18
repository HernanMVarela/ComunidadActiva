package frgp.utn.edu.ar.entidades;

public class ReseniaReporte {
    private Reporte Reporte;
    private Usuario User;
    private int Puntaje;

    public ReseniaReporte(frgp.utn.edu.ar.entidades.Reporte reporte, Usuario user, int puntaje) {
        Reporte = reporte;
        User = user;
        Puntaje = puntaje;
    }

    public ReseniaReporte() {
    }

    public frgp.utn.edu.ar.entidades.Reporte getReporte() {
        return Reporte;
    }

    public void setReporte(frgp.utn.edu.ar.entidades.Reporte reporte) {
        Reporte = reporte;
    }

    public Usuario getUser() {
        return User;
    }

    public void setUser(Usuario user) {
        User = user;
    }

    public int getPuntaje() {
        return Puntaje;
    }

    public void setPuntaje(int puntaje) {
        Puntaje = puntaje;
    }
}
