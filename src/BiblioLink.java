import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;

public class BiblioLink extends Bibliotheque{
/*============================================================================================================ */
/*=========================================== Declaration ===================================================== */
/*============================================================================================================ */
    final static String FICHIER_TXT = "src\\Biblio.txt";
    final static String FICHIER_LINKED_OBJ = "src\\BiblioLinked.obj";
    static BufferedReader tmpBiblio;
    static ObjectOutputStream tmpWriteObj;
    static ObjectInputStream tmpReadObj;
/*============================================================================================================ */
/*=========================================== Constructeur ===================================================== */
/*============================================================================================================ */
    
    private LinkedList<Ouvrage> linkBiblio = new LinkedList<Ouvrage>();
    //private int taille=0;
    public BiblioLink() throws Exception {
        this.linkBiblio = charger();
    }
    public BiblioLink(LinkedList<Ouvrage> linkBiblio) {
        this.setLinkBiblio(linkBiblio);
    }
/*============================================================================================================ */
/*=========================================== Chargement ===================================================== */
/*============================================================================================================ */
public LinkedList<Ouvrage> chargerObj() throws Exception {
		try {
			tmpReadObj = new ObjectInputStream (new FileInputStream (FICHIER_LINKED_OBJ));
			linkBiblio = (LinkedList<Ouvrage>) tmpReadObj.readObject();
            //this.setTaille(linkBiblio.length);
    
		}catch(FileNotFoundException e)
		{
			System.out.println("Fichier introuvable. Vérifiez le chemin et nom du fichier.");
		}catch(IOException e)
		{
			System.out.println("Un probléme est arrivé lors de la manipulation du fichier. V�rifiez vos donn�es.");
            System.out.println(e.getMessage());
		}catch(Exception e)
		{
			System.out.println("Un probléme est arrivé lors du chargement du fichier. Contactez l'administrateur.");
		}finally
		{// Exécuté si erreur ou pas
			tmpReadObj.close();
		}
        return linkBiblio;
	}
    
    public LinkedList<Ouvrage> chargerTxt() {
        int r=0;
        try {
            tmpBiblio = new BufferedReader(new FileReader(FICHIER_TXT));
            String ligne = tmpBiblio.readLine();
            String[] elt = new String[6];
            int i=0;
            while(ligne != null){
                elt = ligne.split(";");
                if(elt[0].equalsIgnoreCase("L") ){
                    linkBiblio.add(new Livre(Integer.parseInt(elt[1]),elt[2],elt[3],elt[4],elt[5]));

                }else if(elt[0].equalsIgnoreCase("P") ){
                    linkBiblio.add(new Periodique(Integer.parseInt(elt[1]),elt[2],elt[3],Integer.parseInt(elt[4]),
                                                Integer.parseInt(elt[5])));
                    
                }else if(elt[0].equalsIgnoreCase("C") ){
                    linkBiblio.add(new CDisque(Integer.parseInt(elt[1]),elt[2],elt[3],elt[4]));
                    
                }
                    ligne = tmpBiblio.readLine();
                i++;
                //r=i;
            }
    } catch (IOException e) {
        
        e.printStackTrace();
    }
    
    //taille = r;
    return linkBiblio;

    }
   
    public LinkedList<Ouvrage> charger() throws Exception {
        
        File file = new File(FICHIER_LINKED_OBJ);
        if(file.exists()){
            return chargerObj();
        }else{
            return chargerTxt();
        }

    }

/*============================================================================================================ */
/*=========================================== Sauvegarde ===================================================== */
/*============================================================================================================ */

    public void sauvegarder() throws IOException {
		try {
			tmpWriteObj = new ObjectOutputStream(new FileOutputStream(FICHIER_LINKED_OBJ));
			tmpWriteObj.writeObject(linkBiblio);
		} catch (FileNotFoundException e) {
			System.out.println("Fichier introuvable. Vérifiez le chemin et nom du fichier.");
		} catch (IOException e) {
			System.out.println("Un probléme est arrivé lors de la manipulation du fichier. V�rifiez vos donn�es.");
		} catch (Exception e) {
			System.out.println("Un probléme est arrivé lors du chargement du fichier. Contactez l'administrateur.");
		} finally {// Exécuté si erreur ou pas
			tmpWriteObj.close();
		}
	}
/*============================================================================================================ */
/*=========================================== SAR SAR SAR ===================================================== */
/*============================================================================================================ */
    @Override
    public boolean Rechercher(int cote) {
        boolean cond =false;
            for(int i=0;i<linkBiblio.size();i++){
                if(linkBiblio.get(i).getCote()==cote){
                    cond =true;
                    break;
                }

            }
        return cond;
    }

    @Override
    public void Ajouter(String typeListe) {
        int cote=0;
        int cond =0;
        String strCote = JOptionPane.showInputDialog(null, "Entrez le numero cote");
        if(strCote==null || strCote.equals(" ")){
            cote=0;
        }else{
            cote = Integer.parseInt(strCote);
        }
        while(cond==0){

            if(Rechercher(cote)){
                strCote = JOptionPane.showInputDialog(null, "Le cote " + cote + " existe deja \n Entrez un autre numero cote");
                if(strCote==null || strCote.equals(" ")){
                    cote=0;
                }else{
                    cote = Integer.parseInt(strCote);
                }
                    }else{
                cond=1;
            }  
        }

        if(cote != 0){   
            String date="";
            String  titre="", auteur="",editeur="";
            int numero=0, periodicite=0;
    
            Dimension d =new Dimension(150,20);
            ArrayList<JTextField> listeJtxt = new ArrayList<>();
            ArrayList<String> listeChamps= new ArrayList<String>();
           
            if(typeListe.equals("livre")){
                listeChamps = new ArrayList<String>(){{add("Date");add("Auteur");add("Titre");add("Editeur");}};
            }else if(typeListe.equals("cd")){
                listeChamps = new ArrayList<String>(){{add("Date");add("Auteur");add("Titre");}};
            }else if(typeListe.equals("periodique")){
                listeChamps = new ArrayList<String>(){{add("Date");add("Nom");add("Numero");add("Periodicite");}};
            }    
            
            JPanel gPane = new JPanel(new GridLayout(listeChamps.size(),1));

            for(String str:listeChamps){
                JPanel pane = new JPanel();
                JTextField jtxt = new JTextField();
                jtxt.setPreferredSize(d);
                JLabel lbl = new JLabel(str);
                lbl.setPreferredSize(d);
                lbl.setLabelFor(jtxt);
                listeJtxt.add(jtxt);
                pane.add(lbl);
                pane.add(jtxt);
                gPane.add(pane);

            }
            int res = JOptionPane.showConfirmDialog(null,gPane);
            if(res == JOptionPane.YES_OPTION){
        //        cote = Integer.parseInt(listeJtxt.get(0).getText());
                date = listeJtxt.get(0).getText();
                auteur = listeJtxt.get(1).getText();

                if(typeListe.equals("livre")){
                    titre = listeJtxt.get(2).getText();
                    editeur =listeJtxt.get(3).getText();
                }else if(typeListe.equals("cd")){
                    titre = listeJtxt.get(2).getText();
                }else if(typeListe.equals("periodique")){
                    numero = Integer.parseInt(listeJtxt.get(2).getText());
                    periodicite = Integer.parseInt(listeJtxt.get(3).getText());
                }    
            
                if(typeListe.equals("livre")){
                    linkBiblio.add(new Livre(cote,date,auteur,titre,editeur));
                }else if(typeListe.equals("cd")){
                    linkBiblio.add(new CDisque(cote,date,auteur,titre));
                }else if(typeListe.equals("periodique")){
                    linkBiblio.add(new Periodique(cote,date,auteur,numero,periodicite));
                }    
                        
                //this.setlinkBiblio(tabTemp);
                //this.setTaille(taille+1);
                        
            }
            
        }
    }
    @Override
    public void Suprimer(int cote) {
        for(int i=0;i<linkBiblio.size();i++){
            if(linkBiblio.get(i).getCote() == cote){
                linkBiblio.remove(i);
                break;

            }
        }
       // this.setlinkBiblio(tabTemp);
        //this.setTaille(taille-1);

        //Lister();
    }
    @Override
    public String toString() {
        
        String strLivre="";
        String strPeriodique="";
        String strCD="";
             
        String retour= "";
        retour= "  Le nombre total des ouvrages "+ linkBiblio.size() +"\n";
        for(Ouvrage ouvrage:linkBiblio){
            if(ouvrage instanceof Livre){
                strLivre += ((Livre) ouvrage).toString();
            }else if(ouvrage instanceof Periodique){
                strPeriodique += ((Periodique) ouvrage).toString();
            }else if(ouvrage instanceof CDisque){
                strCD += ((CDisque) ouvrage).toString();
            }
        }
        retour+= "\n  Les Livres\n  Cote\tDate\t"+ Ouvrage.envollopeMot("Auteur",15)+ Ouvrage.envollopeMot("\ttitre",15) + Ouvrage.envollopeMot("\tEditeur",15)+"\n"+ strLivre;
        retour+= "\n  Les periodiques\n  Cote\tDate\t"+ Ouvrage.envollopeMot("Nom",15)+"\tNumero\tPeriodicite\n"+ strPeriodique;
        retour+= "\n  Les CD\n  Cote\tDate\t"+ Ouvrage.envollopeMot("Titre",15)+ Ouvrage.envollopeMot("\tAuteur",15)+"\n"+ strCD;
        return retour;
    }
    public LinkedList<Ouvrage> getLinkBiblio() {
        return linkBiblio;
    }
    public void setLinkBiblio(LinkedList<Ouvrage> linkBiblio) {
        this.linkBiblio = linkBiblio;
    }
    
    
}