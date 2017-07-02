
package com.a4l.joanbot;

public enum Categorias {
    Ciencia("361", "Ciencia"),
    Curiosiedades("513", "Curiosiedades"),
    Deportes("87", "Deportes"),
    Economía("88", "Economía"),
    Estilo("90", "Estilo"),
    Europa("60", "Europa"),
    Gastronomía("358", "Gastronomía"),
    Internacionales("356", "Internacionales"),
    Motor("365", "Motor"),
    OcioyCultura("89", "Ocio y Cultura"),
    Opinión("357", "Opinión"),
    Política("355", "Política"),
    SaludyBelleza("360", "Salud y Belleza"),
    ShowbizyTV("86", "Showbiz y TV"),
    Sociedad("362", "Sociedad"),
    Sucesos("359", "Sucesos"),
    Tecnología("91", "Tecnología"),
    Tendencias("364", "Tendencias"),
    Viajes("363", "Viajes");

    private final String ID;
    private final String name;

    Categorias(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
