package naampuzzel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Loes
 */
public class Naampuzzel
{
    //Initiëren variabelen
    static char [] alfabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
        'o','p','q','r','s','t','u','v','w','x','y','z'};
    static char [] achternaam = {'v','o','n','b','e','r','g'};
    static char [] nietInNaam = {'d','f','l','u','w','q','x','t','y','m','z'};
    static char [] lettersInNaam = new char [26];
    static int nUniekInNaam = 0; //start op 0, maar wordt correct na vullen lettersInHeleNaam
    static String [] combinaties = new String [100000];
    static String [] populaireNamen = new String[10000]; //lijst met populaire namen 1983-2006 uit csv-bestand
    static String [] populaireNamenSchoon = new String[10000]; //lijst met populaire namen zonder rare tekens
    
    //leest het csv-bestand met populaire namen en slaat deze op in de
    //String array populaireNamen
    static void leesNamenBestand() throws IOException
    {
        //csv-bestand
        String csvBestand = "jongensnamen.csv";
        
        try(BufferedReader br = new BufferedReader(new FileReader(csvBestand)))
        {
            String line;
            for(int i = 0; i < 10000; i++)
            {
                line = br.readLine();
                if(line != null)
                    populaireNamen[i] = line;
                else
                    break;
            }
            populaireNamenSchoon = schrapRareTekens();
        }
        //als het bestand niet geopend kan worden
        catch(IOException e)
        {
            System.out.println(e.toString());
            System.out.println("Kon bestand " + csvBestand + " niet openen.");
        }
    }
   
    static void setLettersInNaam()
    {
        //loop char array alfabet af
        for(int i = 0; i < alfabet.length; i++)
        {
            //System.out.println("\n\nChecken voor de letter " + alfabet[i] + ".");
            
            //variabele om te checken of de letter voorkomt in nietInNaam
            int komtVoor = 0;
            
            //loop char array met letters niet in de naam af
            for(int j = 0; j < nietInNaam.length; j++)
            {
                //check hoeft alleen als er niet eerder gelijkenis is gevonden
                if(komtVoor == 0)
                {
                    //check op verboden letters
                    if(alfabet[i] == nietInNaam[j])
                    {
                        //System.out.println(" dit is dezelfde letter.");
                        komtVoor++;
                    }
                    
                    
                }
            }
            
            for(int k = 0; k < achternaam.length; k++)
            {
                //check hoeft alleen als er niet eerder gelijkenis is gevonden
                if(komtVoor == 0)
                {
                    //check op letters achternaam
                    if(alfabet[i] == achternaam[k])
                        komtVoor++;
                }
            }
            
            //checken of de letter van het alfabet voorkwam in de lijst met letters
            //nietInNaam
            if(komtVoor == 0)
            {
                /*System.out.println("\nDe letter " + alfabet[i] + " komt niet voor in het lijstje"
                        + " met letters die niet in de naam voorkomen en niet in de achternaam en komt dus "
                       + "wel in de naam voor.");*/
                lettersInNaam[nUniekInNaam] = alfabet[i];
                nUniekInNaam++;
            }
        }
        
    }
    
    //doorloopt string array met populaire namen en schrapt namen met tekens anders
    //dan de letters van het alfabet, spaties, streepjes of apostrofs.
    static String[] schrapRareTekens()
    {
        String [] schoneLijst = new String [populaireNamen.length];
        
        //initieer teller voor lijst met "schone" namen
        int tellerSchoon = 0;
        
        //doorloop string array met populaire namen
        for(int i = 0; i < populaireNamen.length; i++)
        {
            //System.out.print("\nCheck de naam " + populaireNamen[i] + ": ");
            
            //houdt bij of er rare tekens in de naam voorkomen
            boolean schoon = true;
            
            if(populaireNamen[i] != null)
            {
                    
                //character array om letters van de naam tijdelijk in op te slaan
                char [] letters = populaireNamen[i].toCharArray();
                
                //doorloop letters van naam
                for(int j = 0; j < letters.length; j++)
                {
                    boolean komtVoor = false;

                    //doorloop alfabet
                    for(int k = 0; k < alfabet.length; k++)
                        //check of de letter echt een letter is
                        if(Character.toLowerCase(letters[j]) == alfabet[k])
                        {
                            //System.out.println(letters[j] + " komt voor in het alfabet en is geen raar teken.");
                            komtVoor = true;
                        }
                    
                    if(!komtVoor)
                    {
                        //System.out.println(letters[j] + " is een raar teken.");
                        schoon = false;
                    }
                }

                //als er geen rare tekens in de naam voorkomen wordt de naam toegevoegd
                //aan de lijst schoneLijst.
                if(schoon)
                {
                    //System.out.println("Deze naam bevat geen rare tekens.");
                    schoneLijst[tellerSchoon] = populaireNamen[i];
                    tellerSchoon++;
                }

                schoon = true;
            }
        }
        
        //print lijst met "schone" namen
        /*System.out.println("De namen zonder rare tekens zijn:");
        for(int i = 0; i < schoneLijst.length; i++)
        {
            if(schoneLijst[i] != null)
                System.out.println(schoneLijst[i]);
        }*/
        
        return schoneLijst;
    }
    
    static boolean uniekeLetters(String naam)
    {
        char [] chars = naam.toCharArray();
        for(int i = 0; i < chars.length - 1; i++)
                for(int k = i+1; k < chars.length; k++)
                    if(Character.toLowerCase(chars[i]) == Character.toLowerCase(chars[k]))
                        return false;
            return true;
    }
    
    static boolean geenVerbodenLetters(String naam)
    {
        char [] chars = naam.toCharArray();
        //doorloop naam
        for(int i = 0; i < chars.length; i++)
            //doorloop rij met verboden letters
            for(int j = 0; j < nietInNaam.length; j++)
                if(Character.toLowerCase(chars[i]) == nietInNaam[j])
                    return false;
        return true;
    }
    
    //komen alle benodigde letters in de namencombinatie voor?
    static boolean compleet(String namen)
    {
        char [] letters = namen.toCharArray();
        
        //doorloop letters die in namen moeten voorkomen
        for(int i = 0; i < nUniekInNaam; i++)
        {
            boolean checkLetter = false;
            for(int j = 0; j < letters.length; j++)
            {
                if(lettersInNaam[i] == Character.toLowerCase(letters[j]))
                {
                    //System.out.println(lettersInNaam[i] + " komt voor.");
                    checkLetter = true;
                }
            }
            if(!checkLetter)
            {
                //System.out.println(lettersInNaam[i] + " komt niet voor.");
                return false;
            }
        }
        
        return true;
    }
        
    static void combineerNamen()
    {
        String naam1, naam2, naam3;
        int nCombinaties = 0;
        
        //loop lijst met populaire jongensnamen langs
        for(int i = 0; i < populaireNamenSchoon.length; i++)
        {
            //als de naam uit alleen unieke tekens bestaat
            if(populaireNamenSchoon[i] != null && uniekeLetters(populaireNamenSchoon[i]) && geenVerbodenLetters(populaireNamenSchoon[i]))
            {
                //eerste naam wordt bepaald
                naam1 = populaireNamenSchoon[i];
                //System.out.print("\nNaam 1: " + naam1);
                
                //als naam1 is bepaald, kijken of er een naam2 gevonden kan worden
                //die aan de eisen voldoet
                for(int j = i + 1; j < populaireNamenSchoon.length; j++)
                {
                    //check of naam1 en naam2 samen alleen unieke letters bevatten
                    //en er geen letters uit de verboden lijst in voorkomen
                    if(populaireNamenSchoon [j] != null && uniekeLetters(naam1 + populaireNamenSchoon[j]) && geenVerbodenLetters(populaireNamenSchoon[j]))
                    {
                        //tweede naam wordt bepaald
                        naam2 = populaireNamenSchoon[j];
                        //System.out.print(", naam 2: " + naam2);
                        
                        //als naam1 en naam2 zijn bepaald, kijken of er een naam3
                        //gevonden kan worden die aan de eisen voldoet
                        for(int k = j + 1; k < populaireNamenSchoon.length; k++)
                        {
                            if(populaireNamenSchoon[k] != null && uniekeLetters(naam1 + populaireNamenSchoon[j] + populaireNamenSchoon[k]) && geenVerbodenLetters(populaireNamenSchoon[k]))
                            {
                                //derde naam wordt bepaald
                                naam3 = populaireNamenSchoon[k];
                                //System.out.println(", naam 3: " + naam3);
                                
                                if(compleet(naam1 + naam2 + naam3))
                                {
                                    combinaties[nCombinaties] = naam1 + " " + naam2 + " " + naam3;
                                    System.out.println(combinaties[nCombinaties]);
                                    nCombinaties++;
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
    }
    
    //print de opgeschoonde lijst met namen uit het csv-bestand
    public static void printPopulaireNamenSchoon()
    {
        System.out.println("Dit zijn alle populaire meisjesnamen die voorkomen in "
                + "de lijst met de 10.000 populairste voornamen tussen 1983 en 2006, "
                + "maar zonder de namen met rare tekens erin:\n");
        for(int i = 0; i < populaireNamenSchoon.length; i++)
        {
            if(populaireNamenSchoon[i] != null)
                System.out.println(populaireNamenSchoon[i]);
        }
    }
    
    //print de volledige lijst met namen in het csv-bestand
    public static void printPopulaireNamen()
    {
        System.out.println("Dit zijn alle populaire meisjesnamen die voorkomen in "
                + "de lijst met de 10.000 populairste voornamen tussen 1983 en 2006:\n");
        for(int i = 0; i < populaireNamen.length; i++)
        {
            if(populaireNamen[i] != null)
                System.out.println(populaireNamen[i]);
        }
    }
    
    /**
     * 
     * Checken of er drie voornamen zijn met alleen unieke letters die in combinatie
     * met de letters van de achternaam enkel de letters "d'Fluwqxtymz" uitsluiten.
     */
    public static void main(String[] args) throws IOException
    {
        //lees csv-bestand met populaire namen
        leesNamenBestand();
        
        //print lijst met populaire jongensnamen
        /*for(int i = 0; i < populaireNamen.length; i++)
            if(populaireNamen[i] != null)
                System.out.println(populaireNamen[i]);*/
        
        //Bepalen welke letters in de volledige naam voorkomen
        setLettersInNaam();
        
        //print letters die in de volledige naam voorkomen
        /*System.out.println("\nDeze letters komen voor in de volledige naam:");
        for(int i = 0; i < nUniekInHeleNaam; i++)
        {
            System.out.print(lettersInHeleNaam[i]);
            if(i != nUniekInHeleNaam - 1)
                System.out.print(", ");
            else
                System.out.print(".\n\n");
        }*/
        
        //test of de bestanden goed in de lijsten worden gezet
        //printPopulaireNamen();
        //printPopulaireNamenSchoon();
       
        //probeer combinaties van namen die alleen unieke en toegestane letters bevatten
        //en zet deze in de lijst mogelijkCombinaties
        combineerNamen();
        
        //System.out.println(compleet("JpHenkOscar"));
    }
    
}
