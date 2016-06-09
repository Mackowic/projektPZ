/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Paint;
import java.util.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author Maciek
 */
class CustomRenderer extends BarRenderer {

    /**
     * The colors.
     */
    private Paint[] colors;

    /**
     * Creates a new renderer.
     *
     * @param colors the colors.
     */
    public CustomRenderer(final Paint[] colors) {
        this.colors = colors;
    }

    /**
     * Returns the paint for an item. Overrides the default behaviour inherited
     * from AbstractSeriesRenderer.
     *
     * @param row the series.
     * @param column the category.
     *
     * @return The item color.
     */
    public Paint getItemPaint(final int row, final int column) {
        return this.colors[column % this.colors.length];
    }
}

public class PDF {

    public void raport_lokalny() throws IOException, ClassNotFoundException, SQLException {

        loginController login = new loginController();
        String zapytanie = "select count(*) as Aktualne from projekty, (SELECT Nazwa, Opis, Status_zadania, idUzytkownika,projekt from zadania where Status_zadania = 'Aktualne' and idUzytkownika = '" + login.uzytkownikID + "') x where projekty.Nazwa = x.projekt";
        String zapytanie1 = "select count(*) as FORTEST from projekty, (SELECT Nazwa, Opis, Status_zadania, idUzytkownika,projekt from zadania where Status_zadania = 'FORTEST' and idUzytkownika = '" + login.uzytkownikID + "') x where projekty.Nazwa = x.projekt";
        String zapytanie2 = "select count(*) as Zakonczone from projekty, (SELECT Nazwa, Opis, Status_zadania, idUzytkownika,projekt from zadania where Status_zadania = 'Zakonczone' and idUzytkownika = '" + login.uzytkownikID + "') x where projekty.Nazwa = x.projekt";

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
        PreparedStatement statment;
        ResultSet result;
        double odp = 0, odp1 = 0, odp2 = 0;
        statment = con.prepareStatement(zapytanie);
        result = statment.executeQuery();
        if (result.next()) {
            odp = result.getDouble("Aktualne");
        }
        statment = con.prepareStatement(zapytanie1);
        result = statment.executeQuery();
        if (result.next()) {
            odp1 = result.getDouble("FORTEST");
        }
        statment = con.prepareStatement(zapytanie2);
        result = statment.executeQuery();
        if (result.next()) {
            odp2 = result.getDouble("Zakonczone");
        }
        //tu tez zmienic na id
        statment = con.prepareStatement("SELECT CONCAT(imie, ' ', nazwisko) as osoba from uzytkownicy WHERE idUzytkownika = '" + login.uzytkownikID + "'");
        result = statment.executeQuery();
        String bbc = "";
        if (result.next()) {
            bbc = result.getString(1);
        }

        DefaultCategoryDataset set2 = new DefaultCategoryDataset();
        set2.setValue(odp, "", "Aktualne");
        set2.setValue(odp1, "", "FORTEST");
        set2.setValue(odp2, "", "Zakonczone");
        JFreeChart chart = ChartFactory.createBarChart("Wszystkie zadania z projektow za ktore odpowiada " + result.getString(1), "Zadania", "Ilosc", set2, PlotOrientation.VERTICAL,
                false,
                true,
                false);

        final CategoryItemRenderer renderer = new CustomRenderer(
                new Paint[]{Color.blue, Color.pink, Color.cyan,
                    Color.yellow, Color.orange, Color.cyan,
                    Color.magenta, Color.blue});

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage("NO DATA!");

        renderer.setItemLabelsVisible(true);
        final ItemLabelPosition p = new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsJPEG(out, chart, 450, 600);
        DateFormat dataformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dataformat1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date data = new Date();
        String fileName = "Raport lokalny " + dataformat1.format(data) + ".pdf";
        try {
            PDRectangle PAGE_SIZE = PDRectangle.A4;
            PDDocument doc = new PDDocument();
            PDFont font = PDType0Font.load(doc, getClass().getResourceAsStream("/fonts/RobotoCondensed-Regular.ttf"));
            PDFont font1 = PDType0Font.load(doc, getClass().getResourceAsStream("/fonts/RobotoCondensed-Bold.ttf"));
            PDPage page = new PDPage(PAGE_SIZE);
            PDPage page1 = new PDPage(PAGE_SIZE);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            //naglowek strona 1
            Naglowek1(content, dataformat, data);
            //stopka strona1
            Stopka(content, doc);
            content.beginText();
            content.setFont(font1, 48);
            content.moveTextPositionByAmount(135, 550);
            content.showText("Raport lokalny");
            content.endText();

            content.beginText();
            content.setFont(font, 22);
            content.moveTextPositionByAmount(30, 250);
            content.showText("Wersja systemu : 1.0");
            content.newLine();
            content.moveTextPositionByAmount(0, 35);
            content.showText("Autor raportu : " + result.getString("osoba"));
            content.endText();
            content.close();

            PDPageContentStream content1 = new PDPageContentStream(doc, page1);

            PDPage page2 = new PDPage(PAGE_SIZE);
            doc.addPage(page2);
            PDPageContentStream content2 = new PDPageContentStream(doc, page2);
            Naglowek1(content2, dataformat, data);
            //stopka strona2
            Stopka(content2, doc);
            content2.beginText();
            content2.setFont(font, 14);
            content2.moveTextPositionByAmount(30, 775);
            content2.showText("Wszystkie projekty za które odpowiada: " + result.getString(1));
            statment = con.prepareStatement("Select Nazwa, Opis, Poczatek, Koniec, ludzie from projekty where idUzytkownika='" + login.uzytkownikID + "'");
            result = statment.executeQuery();
            content2.newLine();
            int liczba = 615;
            content2.moveTextPositionByAmount(0, -15);
            while (result.next()) {
                content2.newLine();
                content2.moveTextPositionByAmount(0, -22);
                liczba += 22;
                //content2.showText("Nazwa : "+result.getString("Nazwa")+" Opis: "+result.getString("Opis")+" Data rozpoczecia: "+result.getString("Poczatek")+" Data zakonczenia: "+result.getString("Koniec"));

                content2.showText("Nazwa : " + result.getString("Nazwa") + " Opis: " + result.getString(1));
                content2.newLine();
                content2.moveTextPositionByAmount(0, -17);
                liczba += 22;
                content2.showText("Data rozpoczecia: " + result.getString("Poczatek") + " Data zakonczenia: " + result.getString("Koniec"));

            }
            content2.endText();
            //          content2.setLineWidth(2);
//        content2.moveTo(10, liczba + 5);
//        content2.lineTo(10, liczba +5);
//        content2.closeAndStroke();
            DateFormat dataformat2 = new SimpleDateFormat("yyyy-MM-dd");
            statment = con.prepareStatement("Select count(*) as 'Aktualne' from projekty where Koniec > '" + dataformat2.format(data) + "' and idUzytkownika='" + login.uzytkownikID + "'");
            result = statment.executeQuery();
            int aktualne = 0, zakonczone = 0;
            if (result.next()) {
                aktualne = result.getInt("Aktualne");
            }
            statment = con.prepareStatement("Select count(*) as 'Zakonczone' from projekty where Koniec < '" + dataformat2.format(data) + "' and idUzytkownika='" + login.uzytkownikID + "'");
            result = statment.executeQuery();
            if (result.next()) {
                zakonczone = result.getInt("Zakonczone");
            }

            DefaultPieDataset pieDataset = new DefaultPieDataset();
            pieDataset.setValue("Aktualne", aktualne);
            pieDataset.setValue("Zakonczone", zakonczone);
            JFreeChart chart1 = ChartFactory.createPieChart("Zestawienia projektó za ktore odpowiada" + bbc, // Title
                    pieDataset, // Dataset
                    true, // Show legend
                    true, // Use tooltips
                    false // Configure chart to generate URLs?
            );

            PiePlot plot1 = (PiePlot) chart1.getPlot();
            plot1.setSectionPaint("Aktualne", Color.blue);
            plot1.setSectionPaint("Zakonczone", Color.yellow);
            plot1.setExplodePercent("Aktualne", 0.10);
            plot1.setSimpleLabels(true);

            PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                    "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
            plot1.setLabelGenerator(gen);

            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG(out1, chart1, 450, 600);
            PDImageXObject img1 = JPEGFactory.createFromStream(doc, new ByteArrayInputStream(out1.toByteArray()));

            content2.close();
            PDPage page3 = new PDPage(PAGE_SIZE);
            doc.addPage(page3);
            PDPageContentStream content3 = new PDPageContentStream(doc, page3);
            Naglowek1(content3, dataformat, data);
            //stopka strona2
            Stopka(content3, doc);
            content3.drawImage(img1, 50, 50);
            content3.close();
            PDPage page4 = new PDPage(PAGE_SIZE);
            doc.addPage(page4);
            PDPageContentStream content4 = new PDPageContentStream(doc, page4);
            Naglowek1(content4, dataformat, data);
            //stopka strona2
            Stopka(content4, doc);
            content4.beginText();
            content4.setFont(font, 14);
            content4.moveTextPositionByAmount(30, 780);
            content4.showText("Wszystkie zadania w projektach za ktore odpowiada:" + bbc);
            statment = con.prepareStatement("SELECT zadania.Nazwa,zadania.Opis,zadania.Status_zadania,zadania.projekt, z.Imie, z.Nazwisko,CONCAT(y.imie, ' ' , y.nazwisko) as 'UZY' FROM zadania , (select Nazwa from projekty where idUzytkownika = '" + login.uzytkownikID + "') x, (SELECT imie, nazwisko, idUzytkownika from uzytkownicy) y,(SELECT imie, nazwisko, idUzytkownika from uzytkownicy where idUzytkownika = '" + login.uzytkownikID + "') z WHERE zadania.projekt = x.Nazwa LIMIT 12"); //poprawic
            result = statment.executeQuery();
            content4.newLine();
            int nw = 850;
            content4.moveTextPositionByAmount(0, -15);
            nw -= 15;
            while (result.next()) {
                content4.newLine();
                nw -= 22;
                content4.moveTextPositionByAmount(0, -22);
                //content2.showText("Nazwa : "+result.getString("Nazwa")+" Opis: "+result.getString("Opis")+" Data rozpoczecia: "+result.getString("Poczatek")+" Data zakonczenia: "+result.getString("Koniec"));
                content4.showText("Nazwa : " + result.getString("zadania.Nazwa"));
                content4.newLine();
                nw -= 17;
                content4.moveTextPositionByAmount(0, -17);
                content4.showText(" Opis: " + result.getString("zadania.Opis") + " Status zadania: " + result.getString("zadania.Status_zadania"));
                content4.newLine();
                nw -= 17;
                content4.moveTextPositionByAmount(0, -17);
                content4.showText(" Projekt: " + result.getString("zadania.projekt") + " Przydzielona osoba do zadania: " + result.getString("UZY"));

            }

            content4.endText();
            content4.close();
            statment = con.prepareStatement("SELECT count(*) as 'liczba' FROM `zadania` where idUzytkownika='" + login.uzytkownikID + "'"); //poprawic
            result = statment.executeQuery();
            if (result.next()) {
            }
            statment = con.prepareStatement("SELECT zadania.Nazwa,zadania.Opis,zadania.Status_zadania,zadania.projekt, z.Imie, z.Nazwisko,CONCAT(y.imie, \" \", y.nazwisko) as \"UZY\" FROM zadania , (select Nazwa from projekty where idUzytkownika = '" + login.uzytkownikID + "') x, (SELECT imie, nazwisko, idUzytkownika from uzytkownicy) y,(SELECT imie, nazwisko, idUzytkownika from uzytkownicy where idUzytkownika = '" + login.uzytkownikID + "') z WHERE zadania.projekt = x.Nazwa LIMIT 12," + result.getInt(1) + "");
            result = statment.executeQuery();
            PDPage page5 = new PDPage(PAGE_SIZE);
            doc.addPage(page5);
            PDPageContentStream content5 = new PDPageContentStream(doc, page5);
            Naglowek1(content5, dataformat, data);
            //stopka strona2
            Stopka(content5, doc);
            content5.beginText();
            content5.setFont(font, 14);
            content5.moveTextPositionByAmount(30, 700);
            while (result.next()) {
                content5.newLine();
                nw -= 22;
                content5.moveTextPositionByAmount(0, -22);
                //content2.showText("Nazwa : "+result.getString("Nazwa")+" Opis: "+result.getString("Opis")+" Data rozpoczecia: "+result.getString("Poczatek")+" Data zakonczenia: "+result.getString("Koniec"));
                content5.showText("Nazwa : " + result.getString("zadania.Nazwa"));
                content5.newLine();
                nw -= 17;
                content5.moveTextPositionByAmount(0, -17);
                content5.showText(" Opis: " + result.getString("zadania.Opis") + " Status zadania: " + result.getString("zadania.Status_zadania"));
                content5.newLine();
                nw -= 17;
                content5.moveTextPositionByAmount(0, -17);
                content5.showText(" Projekt: " + result.getString("zadania.projekt") + " Przydzielona osoba do zadania: " + result.getString("UZY"));
            }
            content5.endText();
            content5.close();
            doc.addPage(page1);
            //naglowek strona 2
            Naglowek1(content1, dataformat, data);
            //stopka strona2
            Stopka(content1, doc);
            PDImageXObject img = JPEGFactory.createFromStream(doc, new ByteArrayInputStream(out.toByteArray()));
            content1.drawImage(img, 50, 50);
            content1.close();
            doc.save(fileName);
            doc.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void raport_globalny() throws IOException, ClassNotFoundException, SQLException {
        String zapytanie = "select count(*) as Aktualne from zadania where Status_zadania='Aktualne'";
        String zapytanie1 = "select count(*) as FORTEST from zadania where Status_zadania='FORTEST'";
        String zapytanie2 = "select count(*) as Zakonczone from zadania where Status_zadania='Zakonczone'";

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
        PreparedStatement statment;
        ResultSet result;
        double odp = 0, odp1 = 0, odp2 = 0;
        statment = con.prepareStatement(zapytanie);
        result = statment.executeQuery();
        if (result.next()) {
            odp = result.getDouble("Aktualne");
        }
        statment = con.prepareStatement(zapytanie1);
        result = statment.executeQuery();
        if (result.next()) {
            odp1 = result.getDouble("FORTEST");
        }
        statment = con.prepareStatement(zapytanie2);
        result = statment.executeQuery();
        if (result.next()) {
            odp2 = result.getDouble("Zakonczone");
        }
loginController login = new loginController();
         statment = con.prepareStatement("SELECT CONCAT(imie, ' ', nazwisko) as osoba from uzytkownicy WHERE idUzytkownika = '" + login.uzytkownikID + "'");
        result = statment.executeQuery();
        String bbc = "";
        if (result.next()) {
            bbc = result.getString(1);
        }
        
        DefaultCategoryDataset set2 = new DefaultCategoryDataset();
        set2.setValue(odp, "", "Aktualne");
        set2.setValue(odp1, "", "FORTEST");
        set2.setValue(odp2, "", "Zakonczone");
        JFreeChart chart = ChartFactory.createBarChart("Wszystkie zadania w bazie", "Zadania", "Ilosc", set2, PlotOrientation.VERTICAL,
                false,
                true,
                false);

        final CategoryItemRenderer renderer = new CustomRenderer(
                new Paint[]{Color.red, Color.blue, Color.green,
                    Color.yellow, Color.orange, Color.cyan,
                    Color.magenta, Color.blue});

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage("NO DATA!");

        renderer.setItemLabelsVisible(true);
        final ItemLabelPosition p = new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER, 45.0
        );
        renderer.setPositiveItemLabelPosition(p);
        plot.setRenderer(renderer);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsJPEG(out, chart, 450, 600);
        DateFormat dataformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dataformat1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date data = new Date();
        String fileName = "Raport globalny " + dataformat1.format(data) + ".pdf";
        try {
            PDRectangle PAGE_SIZE = PDRectangle.A4;
            PDDocument doc = new PDDocument();
            PDFont font = PDType0Font.load(doc, getClass().getResourceAsStream("/fonts/RobotoCondensed-Regular.ttf"));
            PDFont font1 = PDType0Font.load(doc, getClass().getResourceAsStream("/fonts/RobotoCondensed-Bold.ttf"));
            PDPage page = new PDPage(PAGE_SIZE);
            PDPage page1 = new PDPage(PAGE_SIZE);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            //naglowek strona 1
            Naglowek(content, dataformat, data);
            //stopka strona1
            Stopka(content, doc);
            content.beginText();
            content.setFont(font1, 48);
            content.moveTextPositionByAmount(135, 550);
            content.showText("Raport globalny");
            content.endText();

            content.beginText();
            content.setFont(font, 22);
            content.moveTextPositionByAmount(30, 250);
            content.showText("Wersja systemu : 1.0");
            content.newLine();
            content.moveTextPositionByAmount(0, 35);
            content.showText("Autor raportu : " + result.getString("osoba"));
            content.endText();
            content.close();

            PDPageContentStream content1 = new PDPageContentStream(doc, page1);

            PDPage page2 = new PDPage(PAGE_SIZE);
            doc.addPage(page2);
            PDPageContentStream content2 = new PDPageContentStream(doc, page2);
            Naglowek(content2, dataformat, data);
            //stopka strona2
            Stopka(content2, doc);
            content2.beginText();
            content2.setFont(font, 14);
            content2.moveTextPositionByAmount(30, 775);
            content2.showText("Wszystkie projekty z bazy danych:");
            statment = con.prepareStatement("Select Nazwa, Opis, Poczatek, Koniec, ludzie from projekty");
            result = statment.executeQuery();
            content2.newLine();
            int liczba = 615;
            content2.moveTextPositionByAmount(0, -15);
            while (result.next()) {
                content2.newLine();
                content2.moveTextPositionByAmount(0, -22);
                liczba += 22;
                //content2.showText("Nazwa : "+result.getString("Nazwa")+" Opis: "+result.getString("Opis")+" Data rozpoczecia: "+result.getString("Poczatek")+" Data zakonczenia: "+result.getString("Koniec"));

                content2.showText("Nazwa : " + result.getString("Nazwa") + " Opis: " + result.getString("Opis"));
                content2.newLine();
                content2.moveTextPositionByAmount(0, -17);
                liczba += 22;
                content2.showText("Data rozpoczecia: " + result.getString("Poczatek") + " Data zakonczenia: " + result.getString("Koniec"));

            }
            content2.endText();
            //          content2.setLineWidth(2);
//        content2.moveTo(10, liczba + 5);
//        content2.lineTo(10, liczba +5);
//        content2.closeAndStroke();
            DateFormat dataformat2 = new SimpleDateFormat("yyyy-MM-dd");
            statment = con.prepareStatement("Select count(*) as 'Aktualne' from projekty where Koniec > '" + dataformat2.format(data) + "'");
            result = statment.executeQuery();
            int aktualne = 0, zakonczone = 0;
            if (result.next()) {
                aktualne = result.getInt("Aktualne");
            }
            System.out.println("aktualne:"+aktualne);
            statment = con.prepareStatement("Select count(*) as 'Zakonczone' from projekty where Koniec < '" + dataformat2.format(data) + "'");
            result = statment.executeQuery();
            if (result.next()) {
                zakonczone = result.getInt("Zakonczone");
            }
System.out.println("zakonczone:"+zakonczone);
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            pieDataset.setValue("Aktualne", aktualne);
            pieDataset.setValue("Zakonczone", zakonczone);
            JFreeChart chart1 = ChartFactory.createPieChart("Zestawienia projektów", // Title
                    pieDataset, // Dataset
                    true, // Show legend
                    true, // Use tooltips
                    false // Configure chart to generate URLs?
            );

            PiePlot plot1 = (PiePlot) chart1.getPlot();
            plot1.setSectionPaint("Aktualne", Color.green);
            plot1.setSectionPaint("Zakonczone", Color.red);
            plot1.setExplodePercent("Aktualne", 0.10);
            plot1.setSimpleLabels(true);

            PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                    "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
            plot1.setLabelGenerator(gen);

            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsJPEG(out1, chart1, 450, 600);
            PDImageXObject img1 = JPEGFactory.createFromStream(doc, new ByteArrayInputStream(out1.toByteArray()));

            content2.close();
            PDPage page3 = new PDPage(PAGE_SIZE);
            doc.addPage(page3);
            PDPageContentStream content3 = new PDPageContentStream(doc, page3);
            Naglowek(content3, dataformat, data);
            //stopka strona2
            Stopka(content3, doc);
            content3.drawImage(img1, 50, 50);
            content3.close();
            PDPage page4 = new PDPage(PAGE_SIZE);
            doc.addPage(page4);
            PDPageContentStream content4 = new PDPageContentStream(doc, page4);
            Naglowek(content4, dataformat, data);
            //stopka strona2
            Stopka(content4, doc);
            content4.beginText();
            content4.setFont(font, 14);
            content4.moveTextPositionByAmount(30, 780);
            content4.showText("Wszystkie zadania w bazie:");
            statment = con.prepareStatement("SELECT `Nazwa`,`Opis`,`Status_zadania`,`projekt`, CONCAT(x.imie, \" \", x.nazwisko) as \"UZY\" FROM `zadania` , (SELECT imie, nazwisko, idUzytkownika from uzytkownicy) X WHERE zadania.idUzytkownika=x.idUzytkownika limit 12");
            result = statment.executeQuery();
            content4.newLine();
            int nw = 850;
            content4.moveTextPositionByAmount(0, -15);
            nw -= 15;
            while (result.next()) {
                content4.newLine();
                nw -= 22;
                content4.moveTextPositionByAmount(0, -22);
                //content2.showText("Nazwa : "+result.getString("Nazwa")+" Opis: "+result.getString("Opis")+" Data rozpoczecia: "+result.getString("Poczatek")+" Data zakonczenia: "+result.getString("Koniec"));
                content4.showText("Nazwa : " + result.getString("Nazwa"));
                content4.newLine();
                nw -= 17;
                content4.moveTextPositionByAmount(0, -17);
                content4.showText(" Opis: " + result.getString("Opis") + " Status zadania: " + result.getString("Status_zadania"));
                content4.newLine();
                nw -= 17;
                content4.moveTextPositionByAmount(0, -17);
                content4.showText(" Projekt: " + result.getString("projekt") + " Przydzielona osoba do zadania: " + result.getString("UZY"));

            }

            content4.endText();
            content4.close();
            statment = con.prepareStatement("SELECT count(*) as 'liczba' FROM `zadania`");
            result = statment.executeQuery();
            if (result.next()) {
            }
            statment = con.prepareStatement("SELECT `Nazwa`,`Opis`,`Status_zadania`,`projekt`, CONCAT(x.imie, \" \", x.nazwisko) as \"UZY\" FROM `zadania` , (SELECT imie, nazwisko, idUzytkownika from uzytkownicy) X WHERE zadania.idUzytkownika=x.idUzytkownika limit 12," + result.getInt(1) + "");
            result = statment.executeQuery();
            PDPage page5 = new PDPage(PAGE_SIZE);
            doc.addPage(page5);
            PDPageContentStream content5 = new PDPageContentStream(doc, page5);
            Naglowek(content5, dataformat, data);
            //stopka strona2
            Stopka(content5, doc);
            content5.beginText();
            content5.setFont(font, 14);
            content5.moveTextPositionByAmount(30, 700);
            while (result.next()) {
                content5.newLine();
                nw -= 22;
                content5.moveTextPositionByAmount(0, -22);
                //content2.showText("Nazwa : "+result.getString("Nazwa")+" Opis: "+result.getString("Opis")+" Data rozpoczecia: "+result.getString("Poczatek")+" Data zakonczenia: "+result.getString("Koniec"));
                content5.showText("Nazwa : " + result.getString("Nazwa"));
                content5.newLine();
                nw -= 17;
                content5.moveTextPositionByAmount(0, -17);
                content5.showText(" Opis: " + result.getString("Opis") + " Status zadania: " + result.getString("Status_zadania"));
                content5.newLine();
                nw -= 17;
                content5.moveTextPositionByAmount(0, -17);
                content5.showText(" Projekt: " + result.getString("projekt") + " Przydzielona osoba do zadania: " + result.getString("UZY"));
            }
            content5.endText();
            content5.close();
            doc.addPage(page1);
            //naglowek strona 2
            Naglowek(content1, dataformat, data);
            //stopka strona2
            Stopka(content1, doc);
            PDImageXObject img = JPEGFactory.createFromStream(doc, new ByteArrayInputStream(out.toByteArray()));
            content1.drawImage(img, 50, 50);
            content1.close();
            doc.save(fileName);
            doc.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void Naglowek(PDPageContentStream content, DateFormat dataformat, Date data) throws Exception {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 12);
        content.moveTextPositionByAmount(10, 825);
        content.showText("Raport globalny - " + dataformat.format(data));
        content.endText();
    }

    private void Stopka(PDPageContentStream content, PDDocument doc) throws Exception {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 12);
        content.moveTextPositionByAmount(10, 10);
        content.showText("Task Project Manager");
        content.endText();
        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 12);
        content.moveTextPositionByAmount(300, 10);
        int nr = doc.getNumberOfPages();
        content.showText(Integer.toString(nr));
        content.endText();
    }

    private void Naglowek1(PDPageContentStream content, DateFormat dataformat, Date data) throws Exception {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 12);
        content.moveTextPositionByAmount(10, 825);
        content.showText("Raport lokalny - " + dataformat.format(data));
        content.endText();
    }

}
