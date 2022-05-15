import Objects.*;
import structures.TwoThreeDataNode;
import structures.TwoThreeNode;
import structures.TwoThreeTree;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;


public class App {
    public TwoThreeTree<PCR> PCRtests;
    public TwoThreeTree<PCR_id> PCRtestsId;
    public TwoThreeTree<Person> people;
    public TwoThreeTree<Workplace> workplaces;
    public TwoThreeTree<District> districts;
    public TwoThreeTree<Region> regions;


    public App() {
        PCRtests = new TwoThreeTree<>();
        PCRtestsId = new TwoThreeTree<>();
        people = new TwoThreeTree<>();
        workplaces = new TwoThreeTree<>();
        districts = new TwoThreeTree<>();
        regions = new TwoThreeTree<>();
        try {
            //generateData(10,10,10,1000,100000);
        } catch (Exception e) {
            System.out.println(e);
        }
        this.importData();
    }

    public TwoThreeTree<PCR> getPCRtests() {
        return PCRtests;
    }

    public TwoThreeTree<PCR_id> getPCRtestsId() {
        return PCRtestsId;
    }

    public TwoThreeTree<Person> getPeople() {
        return people;
    }

    public TwoThreeTree<Workplace> getWorkplaces() {
        return workplaces;
    }

    public TwoThreeTree<District> getDistricts() {
        return districts;
    }

    public TwoThreeTree<Region> getRegions() {
        return regions;
    }

    public void importData() {
        String file = "people.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String[] dateStr = data[3].split("\\.");
                Date date = new Date((Integer.parseInt(dateStr[2]) - 1900), Integer.parseInt(dateStr[1]) - 1, Integer.parseInt(dateStr[0]));
                people.add(new Person(data[1], data[2], date, data[0]));
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        for (int i = 0; i <= 1000; i++) {
            workplaces.add(new Workplace(i));
        }


        for (int i = 0; i <= 100; i++) {
            districts.add(new District(i));
        }


        for (int i = 0; i <= 10; i++) {
            regions.add(new Region(i));
        }


        file = "pcrTests.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String[] dateStr = data[0].split("\\.");
                Date date = new Date((Integer.parseInt(dateStr[2]) - 1900), Integer.parseInt(dateStr[1]) - 1, Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[3]), Integer.parseInt(dateStr[4]));
                Person person = people.find(new Person(data[2]));
                Workplace workplace = workplaces.find(new Workplace(Integer.parseInt(data[3])));
                District district = districts.find(new District(Integer.parseInt(data[4])));
                Region region = regions.find(new Region(Integer.parseInt(data[5])));
                PCR pcr = new PCR(date, data[1], person, workplace, district, region, (Integer.parseInt(data[6]) == 0 ? false : true), data[7]);

                PCRtests.add(pcr);
                PCRtestsId.add(new PCR_id(pcr.getId(), pcr));
                person.getPCR().add(pcr);
                workplace.getPCR().add(pcr);
                district.getPCR().add(pcr);
                district.getPCR_notes().add(new PCR_notes(pcr.getNotes(), pcr));
                region.getPCR().add(pcr);
                System.out.println("KONIEC VKLADANIA");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void exportData() {
        try  {
            File csvFile = new File("people.csv");
            FileWriter fileWriter = new FileWriter(csvFile);
            ArrayList<Person> p = people.getInOrderArrayL();

            if (p != null) {
                for (Person per: p) {
                    StringBuilder line = new StringBuilder();
                    line.append(per.getRC());
                    line.append(";");
                    line.append(per.getName());
                    line.append(";");
                    line.append(per.getSurname());
                    line.append(";");
                    System.out.println(per.getBirthday());
                    line.append(per.getBirthday().getDate() + "." + (per.getBirthday().getMonth() + 1) + "." + (per.getBirthday().getYear() + 1900));
                    line.append('\n');
                    fileWriter.write(line.toString());
                }
            }
            fileWriter.close();

            /*csvFile = new File("workplaces.csv");
            fileWriter = new FileWriter(csvFile);
            ArrayList<Workplace> w = workplaces.getInOrderArrayL();

            if (w != null) {
                for (Workplace workP: w) {
                    StringBuilder line = new StringBuilder();
                    line.append(workP.getId());
                    line.append('\n');
                    fileWriter.write(line.toString());
                }
            }
            fileWriter.close();

            csvFile = new File("districts.csv");
            fileWriter = new FileWriter(csvFile);
            ArrayList<District> d = districts.getInOrderArrayL();

            if (d != null) {
                for (District dis: d) {
                    StringBuilder line = new StringBuilder();
                    line.append(dis.getId());
                    line.append('\n');
                    fileWriter.write(line.toString());
                }
            }
            fileWriter.close();

            csvFile = new File("regions.csv");
            fileWriter = new FileWriter(csvFile);
            ArrayList<Region> r = regions.getInOrderArrayL();

            if (r != null) {
                for (Region reg: r) {
                    StringBuilder line = new StringBuilder();
                    line.append(reg.getId());
                    line.append('\n');
                    fileWriter.write(line.toString());
                }
            }
            fileWriter.close();*/

            csvFile = new File("pcrTests.csv");
            fileWriter = new FileWriter(csvFile);
            ArrayList<PCR> pcrs = PCRtests.getInOrderArrayL();

            if (pcrs != null) {
                for (PCR pc: pcrs) {
                    StringBuilder line = new StringBuilder();
                    String date = pc.getDateTime().getDate() + "." + (pc.getDateTime().getMonth() + 1) + "." + (pc.getDateTime().getYear() + 1900) + "." + pc.getDateTime().getHours() + "." + pc.getDateTime().getMinutes();
                    line.append(date);
                    line.append(";");
                    line.append(pc.getId());
                    line.append(";");
                    line.append(pc.getPerson().getRC());
                    line.append(";");
                    line.append(pc.getWorkplace().getId());
                    line.append(";");
                    line.append(pc.getDistrict().getId());
                    line.append(";");
                    line.append(pc.getRegion().getId());
                    line.append(";");
                    line.append(pc.getResult() ? 1 : 0);
                    line.append(";");
                    line.append(pc.getNotes());
                    line.append('\n');
                    fileWriter.write(line.toString());
                }
            }
            fileWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateData(/*int countDistricts, int countRegions, int countWorkplaces, */int countPeople, int countTests) throws IOException {
        String[] girlsName = {
                "Adela", "Adriána", "Agáta", "Agnesa", "Alana", "Albína", "Alena", "Alexandra", "Alexia", "Alica", "Alojzia",
                "Alžbeta", "Amália", "Anabela", "Anastázia", "Andrea", "Aneta", "Anežka", "Angela", "Angelika", "Anna", "Antónia", "Aurélia",
                "Auróra", "Barbara", "Barbora", "Beáta", "Berta", "Bianka", "Bibiána", "Blanka", "Blažena", "Bohdana", "Bohumila", "Bohuslava", "Božena", "Božidara", "Branislava\", " +
                "Brigita", "Bronislava", "Cecília", "Dagmara", "Dana", "Danica", "Daniela", "Dária", "Darina", "Daša", "Denisa", "Diana",
                "Dobromila", "Dobroslava", "Dominika", "Dorota", "Drahomíra", "Drahoslava", "Dušana", "Edita", "Ela", "Elena", "Eliána", "Eleonóra",
                "Elisa", "Eliška", "Elvíra", "Ema", "Emília", "Erika", "Ernestína", "Estera", "Etela", "Eugénia", "Eva", "Filoména", "Františka",
                "Gabriela", "Galina", "Gertrúda", "Gréta", "Hana", "Hedviga", "Helena", "Henrieta", "Hermína", "Hilda", "Hortenzia", "Ida",
                "Ingrida", "Irena", "Irma", "Ivana", "Iveta", "Ivica", "Izabela", "Jana", "Jarmila", "Jaromíra", "Jaroslava", "Jela", "Jesika",
                "Johana", "Jolana", "Jozefína", "Judita", "Júlia", "Juliána", "Justína", "Kamila", "Karolína", "Karina", "Katarína", "Kiara",
                "Klára", "Klaudia", "Kornélia", "Kristína", "Kvetoslava", "Ladislava", "Lara", "Laura", "Lea", "Lenka", "Lesana", "Lesia",
                "Liana", "Libuša", "Liliana", "Linda", "Lívia", "Ľubica", "Ľubomíra", "Ľuboslava", "Lucia", "Ľudmila", "Lujza", "Lýdia",
                "Magdaléna", "Malvína", "Marcela", "Margaréta", "Margita", "Mária", "Marianna", "Marína", "Marta", "Martina", "Matilda",
                "Melánia", "Melisa", "Mia", "Michaela", "Milada", "Milena", "Milica", "Miloslava", "Milota", "Miriama", "Miroslava", "Monika",
                "Naďa", "Nadežda", "Natália", "Nataša", "Nela", "Nikola", "Nina", "Noéma", "Nora", "Oľga", "Olívia", "Olympia", "Otília",
                "Oxana", "Patrícia", "Paula", "Paulína", "Pavla", "Perla", "Petra", "Petrana", "Petronela", "Radka", "Radoslava", "Radovana",
                "Rebeka", "Regina", "Renáta", "Rita", "Romana", "Rozália", "Ružena", "Sabína", "Sandra", "Sára", "Sidónia", "Silvia",
                "Simona", "Sláva", "Slávka", "Slavomíra", "Sofia", "Soňa", "Stanislava", "Štefánia", "Stela", "Svetlana", "Tamara", "Táňa",
                "Tatiana", "Tereza", "Terézia", "Tímea", "Uršuľa", "Valentína", "Valéria", "Vanda", "Vanesa", "Veronika", "Viera",
                "Vieroslava", "Viktória", "Vilma", "Viola", "Viviána", "Vladimíra", "Vlasta", "Xénia", "Zara", "Žaneta",
                "Zdenka", "Želmíra", "Zina", "Zita", "Zlatica", "Žofia", "Zoja", "Zora", "Zuzana"
        };
        String[] boysName = {
                "Adam", "Adolf", "Adrián", "Aladár", "Alan", "Albert", "Albín", "Aleš", "Alexander", "Alex", "Alexej", "Alfonz",
                "Alfréd", "Alojz", "Ambróz", "Andreas", "Andrej", "Anton", "Arnold", "Artur", "Arpád", "Atila", "Augustín", "Aurel", "Bartolomej",
                "Belo", "Beňadik", "Benedikt", "Benjamín", "Bernard", "Blahoslav", "Blažej", "Bohdan", "Bohumil", "Bohumír", "Bohuš",
                "Bohuslav", "Boleslav", "Bonifác", "Boris", "Branislav", "Bronislav", "Bruno", "Bystrík", "Ctibor", "Cyprián", "Cyril", "Dalibor",
                "Damián", "Daniel", "Dárius", "Dávid", "Demeter", "Denis", "Dezider", "Dionýz", "Dobroslav", "Dominik", "Drahomír",
                "Drahoslav", "Dušan", "Edmund", "Eduard", "Eliáš", "Emanuel", "Emil", "Erich", "Erik", "Ernest", "Ervín", "Eugen",
                "Fedor", "Félix", "Ferdinand", "Filip", "Florián", "František", "Frederik", "Fridrich", "Gabriel", "Gašpar", "Gejza",
                "Gregor", "Gustáv", "Henrich", "Hubert", "Hugo", "Ignác", "Igor", "Iľja", "Imrich", "Ivan", "Izidor", "Jakub", "Ján",
                "Jarolím", "Jaromír", "Jaroslav", "Jerguš", "Jonáš", "Jozef", "Július", "Juraj", "Justín", "Kamil", "Karol", "Kazimír", "Kevin",
                "Klaudius", "Klement", "Koloman", "Konštantín", "Kornel", "Kristián", "Krištof", "Ladislav", "Leo", "Leonard", "Leopold", "Levoslav",
                "Ľubomír", "Ľubor", "Ľuboš", "Ľuboslav", "Ľudomil", "Ľudovít", "Lukáš", "Marcel", "Marek", "Marián", "Mário", "Marko", "Markus",
                "Maroš", "Martin", "Matej", "Matúš", "Max", "Maxim", "Maximilián", "Medard", "Metod", "Michael", "Michal", "Mikuláš", "Milan",
                "Miloš", "Miloslav", "Miroslav", "Mojmír", "Móric", "Nátan", "Nikolaj", "Nikolas", "Noel", "Norbert", "Oldrich", "Oleg", "Oliver",
                "Ondrej", "Oskár", "Oto", "Pankrác", "Patrik", "Pavol", "Peter", "Pravoslav", "Prokop", "Radomír", "Radoslav",
                "Radovan", "Radúz", "Rastislav", "René", "Richard", "Róbert", "Roland", "Roman", "Ronald", "Rudolf", "Samuel", "Sebastián", "Sergej",
                "Servác", "Severín", "Silvester", "Simon", "Šimon", "Slavomír", "Stanislav", "Štefan", "Svätopluk", "Svetozár", "Tadeáš", "Teo",
                "Teodor", "Tibor", "Tichomír", "Timon", "Timotej", "Tobias", "Tobiáš", "Tomáš", "Urban", "Václav", "Valentín",
                "Valér", "Vasil", "Vavrinec", "Vendelín", "Viktor", "Viliam", "Vincent", "Vít", "Víťazoslav", "Vladimír",
                "Vladislav", "Vlastimil", "Vojtech", "Vratislav", "Vratko", "Zdenko", "Zdeno", "Žigmund", "Zlatko", "Zoltán", "Zoran"
        };

        String[] girlsSurnames = {
                "Višňovská", "Hýroššová", "Trúchlyová", "Majtánová", "Trajčíková", "Beniačová", "Markušová", "Ševčíková", "Valachová", "Jonášová", "Tamášiová", "Kaňová", "Brezánová",
                "Baráneková", "Rajčanová", "Čičkánová", "Pilková", "Čepecová", "Šmehylová", "Držíková",
                "Jakubeková", "Deáková", "Kučerová", "Detková", "Kramerová", "Sopková", "Blažejová", "Ďurišová", "Hulejová", "Cibulková", "Trnovecová",
                "Mandová", "Molitorová", "Veliká", "Kadašová", "Kubicová", "Martinková", "Kemková", "Smiešková", "Valášeková", "Pokorná", "Chochulová", "Novosadová",
                "Šoltýsová", "Varinská", "Tomašecová", "Turská", "Púčeková", "Rakovská", "Hájniková", "Kmeťová", "Slotová", "Bednárová", "Tvrdá", "Cabuková", "Kočanová",
                "Palacková", "Plánková", "Slatinská", "Omastová", "Erdélyiová", "Kopecká", "Tichá", "Kuchareková", "Berníková", "Šípošová", "Sitárová", "Porubská",
                "Hudeková", "Černáková", "Levková", "Mrázová", "Gábrišová", "Ondrušeková", "Smolková", "Šmáriková", "Rudinská", "Krčmárová", "Horecká", "Hurtošová",
                "Hladíková", "Rybáriková", "Kováčiková", "Ponechalová", "Chládeková", "Vrábelová", "Lipovská", "Lemešová", "Velčicová", "Matušíková", "Korbelová", "Koláriková"
        };

        String[] boysSurnames = {
                "Višňovský", "Hýrošš", "Trúchly", "Majtán", "Trajčík", "Beniač", "Markuš", "Ševčík", "Valach", "Jonáš", "Tamáši", "Kaňa", "Brezány",
                "Baránek", "Rajčan", "Čičkán", "Pilka", "Čepec", "Šmehyl", "Držík",
                "Jakubek", "Deák", "Kučera", "Detko", "Kramer", "Sopko", "Blažej", "Ďuriš", "Hulej", "Cibulka", "Trnovec",
                "Manda", "Molitor", "Veliký", "Kadaš", "Kubica", "Martinko", "Kemka", "Smieško", "Valášek", "Pokorný", "Chochul", "Novosad",
                "Šoltýs", "Varinský", "Tomašec", "Turský", "Púček", "Rakovský", "Hájnik", "Kmeťo", "Slota", "Bednár", "Tvrdý", "Cabuk", "Kočan",
                "Palacka", "Plánka", "Slatinský", "Omasta", "Erdélyi", "Kopecký", "Tichý", "Kucharek", "Berník", "Šípoš", "Sitár", "Porubský",
                "Hudek", "Černák", "Levko", "Mráz", "Gábriš", "Ondrušek", "Smolka", "Šmárik", "Rudinský", "Krčmár", "Horecký", "Hurtoš",
                "Hladík", "Rybárik", "Kováčik", "Ponechal", "Chládek", "Vrábel", "Lipovský", "Lemeš", "Velčic", "Matušík", "Korbel", "Kolárik"
        };

        File csvFile = new File("people.csv");
        FileWriter fileWriter = new FileWriter(csvFile);
        ArrayList<Person> rc = new ArrayList<>(countPeople);

        for (int i = 0; i < countPeople; i++) {
            int gender = (Math.random() > 0.5) ? 1 : 0;
            int day = (int)(Math.random() * 28);
            int month = 1 + (int)(Math.random() * 11);
            int year = 1000 + (int)(Math.random() * 1021);
            String fullMonth = "";
            fullMonth += (gender == 1 ? (month + 50) : ((month > 9) ? month : "0" + month));
            String pomRc = (year % 100) + fullMonth + (day > 9 ? day : "0" + day) + "/" + (1000 + (int)(Math.random() * 9000));
            String name = gender == 1 ? girlsName[(int)(Math.random() * girlsName.length)] : boysName[(int)(Math.random() * boysName.length)];
            String surname = gender == 1 ? girlsSurnames[(int)(Math.random() * girlsSurnames.length)] : boysSurnames[(int)(Math.random() * boysSurnames.length)];
            Date birthday = new Date(year - 1900, month - 1, day);

            Person p = new Person(name, surname, birthday, pomRc);
            people.add(p);
            rc.add(p);
        }
        fileWriter.close();

        csvFile = new File("pcrTests.csv");
        fileWriter = new FileWriter(csvFile);
        ArrayList<PCR> pcrs = PCRtests.getInOrderArrayL();

        for (int i = 0; i < countTests; i++) {
            Date date = new Date((int)(Math.random() * (122)), (int)(Math.random() * 11), (int)(1 + Math.random() * 28), (int)(Math.random() * 23), (int)(Math.random() * 59));
            String code = UUID.randomUUID().toString();
            Person person = rc.get((int)(Math.random() * countPeople));
            Workplace workplace;
            do {
                workplace = workplaces.find(new Workplace(1 + (int) (Math.random() * (workplaces.getCount()) - 1)));
            } while (workplace == null);
            District district;
            do {
                district = districts.find(new District(1 + (int)(Math.random() * (districts.getCount()) - 1)));
            } while (district == null);
            Region region;
            do {
                region = regions.find(new Region(1 + (int)(Math.random() * regions.getCount() - 1)));
            } while (region == null);
            boolean result = Math.random() > 0.5;


            PCR pcr = new PCR(date, code, person, workplace, district, region, result, girlsName[(int)(Math.random() * girlsName.length)] + boysName[(int)(Math.random() * boysName.length)]);

            PCRtests.add(pcr);
            PCRtestsId.add(new PCR_id(pcr.getId(), pcr));
            person.getPCR().add(pcr);
            workplace.getPCR().add(pcr);
            district.getPCR().add(pcr);
            district.getPCR_notes().add(new PCR_notes(pcr.getNotes(), pcr));
            region.getPCR().add(pcr);
        }
        fileWriter.close();
    }

    public void deleteAllData() {
        PCRtests.setRoot(null);
        PCRtestsId.setRoot(null);
        people.setRoot(null);
        workplaces.setRoot(null);
        districts.setRoot(null);
        regions.setRoot(null);
    }

    //ULOHA 14
    public String writeRegions(String date, int countDays) {
        ArrayList<Region> arrayRegions = new ArrayList<>(regions.getCount());
        ArrayList<Integer> arrayCounts = new ArrayList<>(regions.getCount());

        Date dateTo;
        if (!date.equals("")) {
            String[] dateToStr = date.split("\\.");
            dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));
        } else {
            dateTo = new Date();
        }
        Date dateFrom = new Date();
        long time = dateTo.getTime() - (countDays * 86400000L);
        dateFrom.setTime(time);

        for (Region region : regions.getInOrderArrayL()) {
            int count = 0;

            TwoThreeTree<PCR> PCRofRegions = region.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
            TwoThreeTree<PCR> positivePCRofRegion = new TwoThreeTree<>();

            if (PCRofRegions == null) {
                PCRofRegions = new TwoThreeTree<>();
            }
            TwoThreeNode<PCR> node = PCRofRegions.getFirstNode();
            TwoThreeDataNode<PCR> dataNode;
            if (node != null) {
                dataNode= new TwoThreeDataNode<PCR>(node.getKeys().get(0), node);
                TwoThreeDataNode<PCR> dataNodeBefore = new TwoThreeDataNode<PCR>(node.getKeys().get(0), node);
                while (dataNode != null) {
                    if (!dataNode.getKey().getResult()) {
                        dataNode = PCRofRegions.getInOrder(dataNode);
                    } else {
                        positivePCRofRegion.add(dataNode.getKey());
                        dataNode = PCRofRegions.getInOrder(dataNode);
                    }
                }
            }

            count = positivePCRofRegion.getCount();

            if (arrayCounts.size() == 0) {
                arrayCounts.add(0, count);
                arrayRegions.add(0, region);
            } else {
                for (int i = 0; i < arrayCounts.size(); i++) {
                    if (arrayCounts.get(i).compareTo(count) >= 0) {
                        arrayCounts.add(i, count);
                        arrayRegions.add(i, region);
                        i = arrayCounts.size();
                    } else if (i == arrayCounts.size() - 1) {
                        arrayCounts.add(i + 1, count);
                        arrayRegions.add(i + 1, region);
                        i++;
                    }
                }
            }

            /*int count = 0;

            TwoThreeTree<PCR> PCRofRegion = region.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
            TwoThreeTree<PCR> positivePCRofRegion = new TwoThreeTree<>();

            if (PCRofRegion != null) {
                TwoThreeNode<PCR> node = PCRofRegion.getFirstNode();
                TwoThreeDataNode<PCR> dataNode = new TwoThreeDataNode<PCR>(node.getKeys().get(0), node);
                TwoThreeDataNode<PCR> dataNodeBefore = new TwoThreeDataNode<PCR>(node.getKeys().get(0), node);
                while (dataNode != null) {
                    if (!dataNode.getKey().getResult()) {
                        PCRofRegion.delete(dataNode.getKey());
                        dataNode.setKey(dataNodeBefore.getKey());
                        dataNode.setNode(PCRofRegion.findNode(dataNodeBefore.getKey()));
                    }
                    dataNode = PCRofRegion.getInOrder(dataNode);
                }
                /*for (PCR pcr : PCRofRegion) {
                    if (pcr.getResult()) {
                        positivePCRofRegion.add(pcr);
                    }
                }
                count = PCRofRegions.getCount();
            } else {
                count = 0;
            }


            if (arrayCounts.size() == 0) {
                arrayCounts.add(0, count);
                arrayRegions.add(0, region);
            } else {
                for (int i = 0; i < arrayCounts.size(); i++) {
                    if (arrayCounts.get(i).compareTo(count) >= 0) {
                        arrayCounts.add(i, count);
                        arrayRegions.add(i, region);
                        i = arrayCounts.size();
                    } else if (i == arrayCounts.size() - 1) {
                        arrayCounts.add(i + 1, count);
                        arrayRegions.add(i + 1, region);
                        i++;
                    }
                }
            }*/
        }

        String text = "";
        for (int i = 0; i < arrayRegions.size(); i++) {
            text += arrayRegions.get(i).getId() + " \t" + arrayCounts.get(i) + "\n";
        }
        return text;
    }

    //ULOHA 13
    public String writeDistricts(String date, int countDays) {
        TwoThreeTree<District> districts = this.districts;
        ArrayList<District> arrayDistricts = new ArrayList<>(districts.getCount());
        ArrayList<Integer> arrayCounts = new ArrayList<>(districts.getCount());

        Date dateTo;
        if (!date.equals("")) {
            String[] dateToStr = date.split("\\.");
            dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));
        } else {
            dateTo = new Date();
        }
        Date dateFrom = new Date();
        long time = dateTo.getTime() - (countDays * 86400000L);
        dateFrom.setTime(time);

        for (District district : districts.getInOrderArrayL()) {
            int count = 0;

            TwoThreeTree<PCR> PCRofDistrict = district.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
            TwoThreeTree<PCR> positivePCRofDistrict = new TwoThreeTree<>();

            if (PCRofDistrict == null) {
                PCRofDistrict = new TwoThreeTree<>();
            }
            TwoThreeNode<PCR> node = PCRofDistrict.getFirstNode();
            TwoThreeDataNode<PCR> dataNode;
            if (node != null) {
                dataNode= new TwoThreeDataNode<PCR>(node.getKeys().get(0), node);
                TwoThreeDataNode<PCR> dataNodeBefore = new TwoThreeDataNode<PCR>(node.getKeys().get(0), node);
                while (dataNode != null) {
                    if (!dataNode.getKey().getResult()) {
                        dataNode = PCRofDistrict.getInOrder(dataNode);
                    } else {
                        positivePCRofDistrict.add(dataNode.getKey());
                        dataNode = PCRofDistrict.getInOrder(dataNode);
                    }
                }
            }

            count = positivePCRofDistrict.getCount();

            if (arrayCounts.size() == 0) {
                arrayCounts.add(0, count);
                arrayDistricts.add(0, district);
            } else {
                for (int i = 0; i < arrayCounts.size(); i++) {
                    if (arrayCounts.get(i).compareTo(count) >= 0) {
                        arrayCounts.add(i, count);
                        arrayDistricts.add(i, district);
                        i = arrayCounts.size();
                    } else if (i == arrayCounts.size() - 1) {
                        arrayCounts.add(i + 1, count);
                        arrayDistricts.add(i + 1, district);
                        i++;
                    }
                }
            }
        }

        String text = "";
        for (int i = 0; i < arrayDistricts.size(); i++) {
            text += arrayDistricts.get(i).getId() + " \t" + arrayCounts.get(i) + "\n";
        }
        return text;
    }

    //ULOHA 19
    public String deletePerson(String rc) {
        Person person = this.people.find(new Person(rc));
        if (person == null) {
            return "Osoba s daným rodným čislom sa nenašla.";
        }

        String text = "Osoba bola vymazaná.\n";
        text += "Rodné číslo: " + person.getRC() + "\n";
        text += "Meno: " + person.getName() + "\n";
        text += "Priezvisko: " + person.getSurname() + "\n";
        text += "Dátum narodenia: " + person.getBirthday().getDate() + "." + (person.getBirthday().getMonth() + 1) + "." + (person.getBirthday().getYear() + 1900) + "\n";

        ArrayList<PCR> arrayPerson = person.getPCR().getInOrderArrayL();
        if (arrayPerson != null) {
            for (PCR pcr : arrayPerson) {
                PCR_id pcrId = this.PCRtestsId.find(new PCR_id(pcr.getId()));
                PCR pcr1 = this.PCRtests.find(pcr);

                Workplace workplace = this.workplaces.find(new Workplace(pcr.getWorkplace().getId()));
                workplace.getPCR().delete(pcr1);
                District district = this.districts.find(new District(pcr.getDistrict().getId()));
                district.getPCR().delete(pcr1);
                Region region = this.regions.find(new Region(pcr.getRegion().getId()));
                region.getPCR().delete(pcr1);

                this.PCRtestsId.delete(pcrId);
                this.PCRtests.delete(pcr1);

            }
        }
        this.people.delete(person);

        return text;
    }

    //ULOHA 18
    public String deletePCR(String idPCR) {
        PCR_id pcrId = this.PCRtestsId.find(new PCR_id(idPCR));
        PCR pcr = null;
        if (pcrId == null) {
            return "Test PCR s daným id sa nenašiel.";
        } else {
            pcr = pcrId.getPCR();
        }

        String text = "Test bol vymazany.\n";
        text += "Kód PCR: " + pcr.getId() + "\n";
        text += "Dátum a čas: " + pcr.getDateTime().getDate() + "." + (pcr.getDateTime().getMonth() + 1) + "." + (pcr.getDateTime().getYear() + 1900) + " " + pcr.getDateTime().getHours() + ":" + pcr.getDateTime().getMinutes() + "\n";
        text += "Pracovisko: " + pcr.getWorkplace().getId() + "\n";
        text += "Kraj: " + pcr.getRegion().getId() + "\n";
        text += "Okres: " + pcr.getDistrict().getId() + "\n";
        text += "Výsledok testu: " + pcr.getResult() + "\n";
        text += "Poznámka: " + pcr.getNotes() + "\n" + "\n";

        text += "Rodné číslo: " + pcr.getPerson().getRC() + "\n";
        text += "Meno: " + pcr.getPerson().getName() + "\n";
        text += "Priezvisko: " + pcr.getPerson().getSurname() + "\n";
        text += "Dátum narodenia: " + pcr.getPerson().getBirthday().getDate() + "." + (pcr.getPerson().getBirthday().getMonth() + 1) + "." + (pcr.getPerson().getBirthday().getYear() + 1900) + "\n";

        Person person = this.people.find(new Person(pcr.getPerson().getRC()));
        person.getPCR().delete(pcr);
        Workplace workplace = this.workplaces.find(new Workplace(pcr.getWorkplace().getId()));
        workplace.getPCR().delete(pcr);
        District district = this.districts.find(new District(pcr.getDistrict().getId()));
        district.getPCR().delete(pcr);
        Region region = this.regions.find(new Region(pcr.getRegion().getId()));
        region.getPCR().delete(pcr);

        this.PCRtestsId.delete(pcrId);
        this.PCRtests.delete(pcr);
        return text;
    }

    //ULOHA 10, 11, 12
    public ArrayList<PCR> searchPeople(String idDistrict, String idRegion, String date, int spCountDaysPeople) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();
        ArrayList<PCR> ResultPcr2 = new ArrayList<>();

        if (!idDistrict.equals("")) {
            District district = this.districts.find(new District(Integer.parseInt(idDistrict)));
            if (district != null) {
                pomPcr = district.getPCR();
                ResultPcr = pomPcr.getInOrderArrayL();
            } else {
                pomPcr = null;
            }
        }
        if (!idRegion.equals("")) {
            Region region = this.regions.find(new Region(Integer.parseInt(idRegion)));
            if (region != null && region.getPCR().getCount() != 0) {
                pomPcr = region.getPCR();
                ResultPcr = pomPcr.getInOrderArrayL();
            } else {
                pomPcr = null;
            }
        }
        if (ResultPcr == null) {
            return null;
        }

            TwoThreeTree<PCR> prcTests = new TwoThreeTree<>();
            if (pomPcr == null) {
                return null;
            } else if (ResultPcr != null && ResultPcr.size() != 0) {
                prcTests = pomPcr;
            } else {
                prcTests = this.PCRtests;
            }

            Date dateTo = new Date();
            if (!date.equals("")) {
                String[] dateToStr = date.split("\\.");
                dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));
            }
            Date dateFrom = new Date();
            long time = dateTo.getTime() - (spCountDaysPeople * 86400000L);
            dateFrom.setTime(time);

            if (dateFrom.compareTo(dateTo) < 0) {
                TwoThreeTree<PCR> tree = prcTests
                        .findInterval(new PCR(dateFrom), new PCR(dateTo));
                if (tree != null) {
                    ResultPcr = tree.getInOrderArrayL();
                } else {
                    ResultPcr = null;
                }

                if (ResultPcr == null || ResultPcr.size() == 0) {
                    pomPcr = null;
                }
            } else {
                pomPcr = null;
            }
            if (pomPcr != null && ResultPcr != null && ResultPcr.size() != 0) {
                for (PCR pcr : ResultPcr) {
                    if (pcr.getResult()) {
                        ResultPcr2.add(pcr);
                    }
                }
            }
            ResultPcr = ResultPcr2;
            if (pomPcr != null && ResultPcr != null && ResultPcr.size() != 0) {
                return ResultPcr;
            } else {
                return null;
            }
    }

    //ULOHA 17
    public void addPerson(String name, String surname, int year, int month, int day, String rc) {
        Person person = new Person(name, surname, new Date(year - 1900, month - 1, day), rc);
        if (this.people.find(person) == null) {
            this.people.add(person);
            showMessageDialog(null, "Osoba s rodným číslo " + rc + " je vytvorená.");
        } else {
            showMessageDialog(null, "Osoba s rodným číslo " + rc + " už existuje.");
        }
    }

    //ULOHA 1
    public void addPCR(int year, int month, int day, int hour, int minute, String rc, String idWorkplace, String idDistrict, String idRegion, int result, String notes) {
        UUID uuid = UUID.randomUUID();

        Date dateTime = new Date((year - 1900), month - 1, day, hour, minute);
        Person person = this.people.find(new Person(rc));
        Workplace workplace = this.workplaces.find(new Workplace(Integer.parseInt(idWorkplace)));
        District district = this.districts.find(new District(Integer.parseInt(idDistrict)));
        Region region = this.regions.find(new Region(Integer.parseInt(idRegion)));
        PCR pcr = new PCR(dateTime, uuid.toString(), person, workplace, district, region, result == 1, notes);

        this.PCRtests.add(pcr);
        this.PCRtestsId.add(new PCR_id(pcr.getId(), pcr));
        if (person == null) {
            showMessageDialog(null, "Osoba s rodným číslo " + rc + " neexistuje! Vytvorte najskôr osobu.");
            return;
        }
        System.out.println(uuid);
        person.getPCR().add(pcr);
        workplace.getPCR().add(pcr);
        district.getPCR().add(pcr);
        region.getPCR().add(pcr);

        showMessageDialog(null, "PCR test s id " + uuid + " je vytvorený.");
    }

    //ULOHA 2, 16
    public ArrayList<PCR> findPCRById(String idPcr, String rc) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();
        ArrayList<PCR> ResultPcr2 = new ArrayList<>();

        PCR_id pcr = this.PCRtestsId.find(new PCR_id(idPcr));
        if (pcr != null) {
            if (!Objects.equals(rc, "")) {
                if (pcr.getPCR().getPerson().getRC().compareTo(rc) == 0) {
                    pomPcr.add(pcr.getPCR());
                    ResultPcr.add(pcr.getPCR());
                } else {
                    pomPcr = null;
                }
            } else {
                pomPcr.add(pcr.getPCR());
                ResultPcr.add(pcr.getPCR()
                );
            }
        } else {
            pomPcr = null;
        }
        if (pomPcr != null) {
            return ResultPcr;
        } else {
            return null;
        }
    }

    //ULOHA 4, 5
    public ArrayList<PCR> findPCRByDistrict(int idDistrict, String dateF, String dateT, int result) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();
        ArrayList<PCR> ResultPcr2 = new ArrayList<>();

        District district = this.districts.find(new District(idDistrict));
        if (district != null) {
            if (!Objects.equals(dateF, "") && !Objects.equals(dateT, "")) {
                String[] dateFromStr = dateF.split("\\.");
                String[] dateToStr = dateT.split("\\.");
                Date dateFrom = new Date(Integer.parseInt(dateFromStr[2]) - 1900, Integer.parseInt(dateFromStr[1]) - 1, Integer.parseInt(dateFromStr[0]));
                Date dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));

                if (dateFrom.compareTo(dateTo) < 0) {
                    TwoThreeTree<PCR> tree = district.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
                    if (tree == null) {
                        ResultPcr = null;
                    } else {
                        ResultPcr = tree.getInOrderArrayL();
                    }
                    if (ResultPcr == null || ResultPcr.size() == 0) {
                        pomPcr = null;
                    }
                } else {
                    pomPcr = null;
                }
            } else {
                pomPcr = district.getPCR();
                ResultPcr = district.getPCR().getInOrderArrayL();
            }

            boolean pom;
            if (/*!tfResult.getText().isEmpty()*/result != -1 && pomPcr != null) {
                for (PCR pcr : ResultPcr) {
                    if (pcr.getResult() == (Objects.equals(/*tfResult.getText()*/result, 1))) {
                        ResultPcr2.add(pcr);
                    }
                }
                ResultPcr = ResultPcr2;
            }
        } else {
            pomPcr = null;
        }
        if (pomPcr != null && ResultPcr != null) {
            return ResultPcr;
        } else {
            return null;
        }
    }

    //ULOHA 15
    public ArrayList<PCR> findPCRByWorplace(int idWorkplace, String dateF, String dateT) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();
        ArrayList<PCR> ResultPcr2 = new ArrayList<>();

        Workplace workplace = this.workplaces.find(new Workplace(idWorkplace));
        if (workplace != null) {
            if (!Objects.equals(dateF, "") && !Objects.equals(dateT, "")) {
                String[] dateFromStr = dateF.split("\\.");
                String[] dateToStr = dateT.split("\\.");
                Date dateFrom = new Date(Integer.parseInt(dateFromStr[2]) - 1900, Integer.parseInt(dateFromStr[1]) - 1, Integer.parseInt(dateFromStr[0]));
                Date dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));

                if (dateFrom.compareTo(dateTo) < 0) {
                    TwoThreeTree<PCR> tree = workplace.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
                    if (tree == null) {
                        ResultPcr = null;
                    } else {
                        ResultPcr = tree.getInOrderArrayL();
                    }
                    if (ResultPcr.size() == 0) {
                        pomPcr = null;
                    }
                } else {
                    pomPcr = null;
                }
            } else {
                pomPcr = workplace.getPCR();
                ResultPcr = workplace.getPCR().getInOrderArrayL();
            }
        } else {
            pomPcr = null;
        }
        if (pomPcr != null && ResultPcr != null) {
            return ResultPcr;
        } else {
            return null;
        }
    }

    //ULOHA 6, 7
    public ArrayList<PCR> findPCRByRegion(int idRegion, String dateF, String dateT, int result) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();
        ArrayList<PCR> ResultPcr2 = new ArrayList<>();

        Region region = this.regions.find(new Region(idRegion));
        if (region != null) {
            if (!dateF.equals("") && !dateT.equals("")) {
                String[] dateFromStr = dateF.split("\\.");
                String[] dateToStr = dateT.split("\\.");
                Date dateFrom = new Date(Integer.parseInt(dateFromStr[2]) - 1900, Integer.parseInt(dateFromStr[1]) - 1, Integer.parseInt(dateFromStr[0]));
                Date dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));

                if (dateFrom.compareTo(dateTo) < 0) {
                    TwoThreeTree<PCR> tree = region.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
                    if (tree == null) {
                        ResultPcr = null;
                    } else {
                        ResultPcr = tree.getInOrderArrayL();
                    }
                    if (ResultPcr.size() == 0) {
                        pomPcr = null;
                    }
                } else {
                    pomPcr = null;
                }
            } else {
                pomPcr = region.getPCR();
                ResultPcr = region.getPCR().getInOrderArrayL();
            }

            if (result != -1) {
                for (PCR pcr : ResultPcr) {
                    if (pcr.getResult() == (result == 1)) {
                        ResultPcr2.add(pcr);
                    }
                }
                ResultPcr = ResultPcr2;
            }
        } else {
            pomPcr = null;
        }
        if (pomPcr != null && ResultPcr != null) {
            return ResultPcr;
        } else {
            return null;
        }
    }

    //ULOHA 3
    public ArrayList<PCR> findPCRByRC(String rc, String dateF, String dateT) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();

        Person person = this.people.find(new Person(rc));
        if (person != null) {
            if (!dateF.equals("")) {
                String[] dateFromStr = dateF.split("\\.");
                String[] dateToStr = dateT.split("\\.");
                Date dateFrom = new Date(Integer.parseInt(dateFromStr[2]) - 1900, Integer.parseInt(dateFromStr[1]) - 1, Integer.parseInt(dateFromStr[0]));
                Date dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));

                if (dateFrom.compareTo(dateTo) < 0) {
                    TwoThreeTree<PCR> tree = person.getPCR().findInterval(new PCR(dateFrom), new PCR(dateTo));
                    if (tree == null) {
                        ResultPcr = null;
                    } else {
                        ResultPcr = tree.getInOrderArrayL();
                    }
                    if (ResultPcr.size() == 0) {
                        pomPcr = null;
                    }
                } else {
                    pomPcr = null;
                }
            } else {
                pomPcr = person.getPCR();
                ResultPcr = person.getPCR().getInOrderArrayL();
            }
        } else {
            pomPcr = null;
        }
        if (pomPcr != null && ResultPcr != null) {
            return ResultPcr;
        } else {
            return null;
        }
    }

    //ULOHA 8, 9
    public ArrayList<PCR> findPCRByDate(String dateF, String dateT, int result) {
        TwoThreeTree<PCR> pomPcr = new TwoThreeTree<>();
        ArrayList<PCR> ResultPcr = new ArrayList<>();
        ArrayList<PCR> ResultPcr2 = new ArrayList<>();

        String[] dateFromStr = dateF.split("\\.");
        String[] dateToStr = dateT.split("\\.");
        Date dateFrom = new Date(Integer.parseInt(dateFromStr[2]) - 1900, Integer.parseInt(dateFromStr[1]) - 1, Integer.parseInt(dateFromStr[0]));
        Date dateTo = new Date(Integer.parseInt(dateToStr[2]) - 1900, Integer.parseInt(dateToStr[1]) - 1, Integer.parseInt(dateToStr[0]));

        if (dateFrom.compareTo(dateTo) < 0) {
            TwoThreeTree<PCR> tree = this.PCRtests.findInterval(new PCR(dateFrom), new PCR(dateTo));
            if (tree == null) {
                ResultPcr = null;
            } else {
                ResultPcr = tree.getInOrderArrayL();
            }
            if (ResultPcr.size() == 0) {
                pomPcr = null;
            } else {
                if (result != -1) {
                    for (PCR pcr : ResultPcr) {
                        if (pcr.getResult() == (result == 1)) {
                            ResultPcr2.add(pcr);
                        }
                    }
                    ResultPcr = ResultPcr2;
                }
            }
        } else {
            pomPcr = null;
        }
        if (pomPcr != null && ResultPcr != null) {
            return ResultPcr;
        } else {
            return null;
        }
    }

    //BONUS
    public ArrayList<PCR_notes> bonus(int idDistrict) {
        District district = this.getDistricts().find(new District(idDistrict));
        TwoThreeTree<PCR_notes> pcr_notes = district.getPCR_notes();

        return pcr_notes.getInOrderArrayL();
    }
}
