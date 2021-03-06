package RacunovodstvoGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import Entity.RadniZadatak;
import Kontroleri.RadniZadaciRacunovodstvoControler;
import UtilClasses.DateLabelFormatter;
import UtilClasses.KorisnickoUputstvo;


public class RadniZadaciRacunovodstvoGUI extends JFrame{
	
	private Date datumKreiranja=null, datumPreuzimanja=null, datumIzvrsenja=null, krajnjiDatumIzvrsenja=null;
	private String ime="", prezime="";
	private Boolean klijentOdabran = false, serviserSelektovan = false;
	private int indexKlijent, indexTabela;
	private JTable tabela;
	private List<RadniZadatak> zadaci = new ArrayList<RadniZadatak>();
	private List<String> naziviServisera = new ArrayList<String>();
	private RadniZadaciRacunovodstvoGUI mySelf;
	private static RadniZadaciRacunovodstvoGUI instanca;
	public RadniZadaciRacunovodstvoGUI() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			initialize();
		}
		public static RadniZadaciRacunovodstvoGUI dajInstancu() {
			if(instanca==null) {
 			instanca=new RadniZadaciRacunovodstvoGUI();
 			
			}
			return instanca;
		}
	public static void unistiInstancu() { instanca= null; }
		// Metoda potrebna za provjeru da li je korisnik odabrao servisere putem
		// forme Odabir servisera
		public void setServiserSelektovan(Boolean serviserSelektovan) {
			this.serviserSelektovan = serviserSelektovan;
		}

		public Boolean getServiserSelektovan() {
			return this.serviserSelektovan;
		}
	
	private static final long serialVersionUID = 1L;
	public void initialize() {
	mySelf = this;
	final RadniZadaciRacunovodstvoControler controler1 = new RadniZadaciRacunovodstvoControler();
	setTitle("Upravljanje radnim zadacima");
	
	JMenuBar glavniMenuBar = new JMenuBar();
	setJMenuBar(glavniMenuBar);
	
	JMenu mojRacunMenu = new JMenu("Moj račun");
	glavniMenuBar.add(mojRacunMenu);
	JMenuItem promijeniSifruItem = new JMenuItem("Promijeni šifru");
	promijeniSifruItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						@SuppressWarnings("unused")
						JFrame frmPromjenaifre = new JFrame();
						@SuppressWarnings("unused")
						PromjenaSifreGUI window = new PromjenaSifreGUI();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	});
	mojRacunMenu.add(promijeniSifruItem);
	JMenuItem odjavaItem = new JMenuItem("Odjavi se");
	odjavaItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
					OdjaviSeGUI ex = new OdjaviSeGUI();
		             ex.setSize(600, 150);
		             ex.setLocationRelativeTo(null);
		             ex.setVisible(true);
				}
			});
		}
	});
	mojRacunMenu.add(odjavaItem);
	
	JMenu alatiMenu = new JMenu("Alati");
	glavniMenuBar.add(alatiMenu);		
	JMenuItem sistemObavjestavanjaItem = new JMenuItem("Sistem obavještavanja");
	sistemObavjestavanjaItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
					SistemObavjestavanjaGUI ex = new SistemObavjestavanjaGUI();
					ex.setVisible(true);
				}
			});
		}
	});
	alatiMenu.add(sistemObavjestavanjaItem);
	
	JMenu pomocMenu = new JMenu("Pomoć");
	glavniMenuBar.add(pomocMenu);		
	JMenuItem oNamaItem = new JMenuItem("O nama");
	oNamaItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                ONamaGUI ex = new ONamaGUI();
	                ex.setSize(300, 150);
	                ex.setLocationRelativeTo(null);
	                ex.setVisible(true);
	            }
	        });
		}
	});
	pomocMenu.add(oNamaItem);		
	JMenuItem korisnickoUputstvoItem = new JMenuItem("Korisničko uputstvo");
	korisnickoUputstvoItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			KorisnickoUputstvo kp = new KorisnickoUputstvo();
			kp.dobaviUputstvo();
		}
	});
	pomocMenu.add(korisnickoUputstvoItem);
	
	JPanel centralniPanel = new JPanel();
	centralniPanel.setLayout(new GridLayout(2,1,0,0));
	
	JPanel filter1Panel = new JPanel();
	JPanel filter2Panel = new JPanel();
	filter1Panel.setLayout(new GridLayout(1,6,2,2));
	filter2Panel.setLayout(new GridLayout(1,6,2,2));
		
	JLabel imePrezimeLbl = new JLabel("Ime i prezime servisera:");
	imePrezimeLbl .setHorizontalAlignment(SwingConstants.RIGHT);
	JLabel nazivKlijentaLbl = new JLabel("Naziv klijenta:");
	nazivKlijentaLbl.setHorizontalAlignment(SwingConstants.RIGHT);
	JLabel datumKreiranjaLbl = new JLabel("Datum kreiranja:");
	datumKreiranjaLbl.setHorizontalAlignment(SwingConstants.RIGHT);
	JLabel datumDodjeleLbl = new JLabel("Datum dodjele:");
	datumDodjeleLbl.setHorizontalAlignment(SwingConstants.RIGHT);
	JLabel datumIzvrsenjaLbl = new JLabel("Datum izvršenja:");
	datumIzvrsenjaLbl.setHorizontalAlignment(SwingConstants.RIGHT);
	JLabel datumZavrsniLbl = new JLabel("Krajnji datum za izvršenje:");
	datumZavrsniLbl.setHorizontalAlignment(SwingConstants.RIGHT);
	
	final JTextField imePrezimeTxt = new JTextField();
	UtilDateModel model1 = new UtilDateModel();
	final JDatePanelImpl datePanel1 = new JDatePanelImpl(model1);
	final JDatePickerImpl datumKreiranjaDP = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
	UtilDateModel model2 = new UtilDateModel();
	final JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
	final JDatePickerImpl datumPruzimanjaDP = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
	UtilDateModel model3 = new UtilDateModel();
	final JDatePanelImpl datePanel3 = new JDatePanelImpl(model3);
	final JDatePickerImpl datumIzvrsenjaDP = new JDatePickerImpl(datePanel3, new DateLabelFormatter());
	UtilDateModel model4 = new UtilDateModel();
	final JDatePanelImpl datePanel4 = new JDatePanelImpl(model4);
	final JDatePickerImpl datumZavrsniDP = new JDatePickerImpl(datePanel4, new DateLabelFormatter());
	
	filter1Panel.add(imePrezimeLbl);
	filter1Panel.add(imePrezimeTxt);
	filter1Panel.add(nazivKlijentaLbl);
	
	final JComboBox<String> nazivKlijentaCmb = new JComboBox<String>();
	try {
		controler1.setKlijenti();
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	nazivKlijentaCmb.addItem("Odaberite klijenta");
	for (int i = 0; i < controler1.getKlijenti().size(); i++)
		nazivKlijentaCmb.addItem(controler1.getKlijenti().get(i).getNaziv());
	
	filter1Panel.add(nazivKlijentaCmb);
	filter1Panel.add(datumKreiranjaLbl);
	filter1Panel.add(datumKreiranjaDP);
	filter2Panel.add(datumDodjeleLbl);
	filter2Panel.add(datumPruzimanjaDP);	
	filter2Panel.add(datumIzvrsenjaLbl);
	filter2Panel.add(datumIzvrsenjaDP);	
	filter2Panel.add(datumZavrsniLbl);
	filter2Panel.add(datumZavrsniDP);	
	
	JPanel filter3Panel = new JPanel();
	filter3Panel.setLayout(new GridLayout(1,6,1,1));
	final JCheckBox nedodjeljenZadatakChckbx = new JCheckBox("Nedodjeljen zadatak");
	nedodjeljenZadatakChckbx.setHorizontalAlignment(SwingConstants.RIGHT);
	filter3Panel.add(nedodjeljenZadatakChckbx);
	
	final JCheckBox neizvrsenZadatakChckbx = new JCheckBox("Neizvršen zadatak");
	neizvrsenZadatakChckbx.setHorizontalAlignment(SwingConstants.RIGHT);
	filter3Panel.add(neizvrsenZadatakChckbx);
	
	final JCheckBox neprihvacenZadatakChckbx = new JCheckBox("Neprihvaćen zadatak");
	neprihvacenZadatakChckbx.setHorizontalAlignment(SwingConstants.RIGHT);
	filter3Panel.add(neprihvacenZadatakChckbx);
	
	JLabel prazno1Lbl = new JLabel("");
	filter3Panel.add(prazno1Lbl);
	
	JLabel prazno2Lbl = new JLabel("");
	filter3Panel.add(prazno2Lbl);
	
	
	JToolBar glavniToolBar = new JToolBar();
	ImageIcon dodajIkona = new ImageIcon(getClass().getResource("AddIcon.png"));
	JButton kreirajBtn = new JButton(dodajIkona);
	kreirajBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                KreiranjeZadatkaGUI ex = KreiranjeZadatkaGUI.dajInstancu();
	                ex.setSize(351, 276);
	                ex.setLocationRelativeTo(null);
	                ex.setVisible(true);
	            }
	        });
		}
	});
	kreirajBtn.setText("Novi zadatak");
	glavniToolBar.add(kreirajBtn);
	JButton evidencijaBtn = new JButton("Evidencija rada");
	
	evidencijaBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                EvidencijaRadaRacunovodstvoGUI f = EvidencijaRadaRacunovodstvoGUI.dajInstancu();
	                f.setTitle("Evidencija rada");
	                f.setSize(1000, 350);
	                f.setLocationRelativeTo(null);
	                f.setVisible(true);
	            }
	     });
		}
	});
	ImageIcon tekstIkona = new ImageIcon(getClass().getResource("TextIcon.png"));
	evidencijaBtn.setIcon(tekstIkona);
	glavniToolBar.add(evidencijaBtn);

	@SuppressWarnings("serial")
	final DefaultTableModel modelTabela = new DefaultTableModel(){
	    @Override
	    public boolean isCellEditable(int row, int column) {
	       return false;
	    }
	};
	
	tabela = new JTable(modelTabela);
	modelTabela.addColumn("Vrsta zadatka");
	modelTabela.addColumn("Opis zadatka");
	modelTabela.addColumn("Klijent");
	modelTabela.addColumn("Serviser(i)");
	tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
	JScrollPane tabelaPane = new JScrollPane(tabela);
	
	try {
		controler1.PretraziSve();
		zadaci.addAll(controler1.getRadniZadaci());
		Ispisi(controler1.getRedovi(controler1.getRadniZadaci()), modelTabela);
	} catch (Exception e) {
		JOptionPane.showMessageDialog(rootPane,
				"Pojavila se greška. Pokušajte ponovo.",
				"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
	}
	
	
	JButton pretraziBtn = new JButton("Pretraži");
	ImageIcon traziIkona = new ImageIcon(getClass().getResource("SearchIcon.png"));
	pretraziBtn.setIcon(traziIkona);
	
	filter3Panel.add(pretraziBtn);
	
	JPanel sjeverniPanel = new JPanel();
	sjeverniPanel.setLayout(new GridLayout(4,1,2,2));
	sjeverniPanel.add(glavniToolBar);
	
	sjeverniPanel.add(filter1Panel);
	sjeverniPanel.add(filter2Panel);
	sjeverniPanel.add(filter3Panel);
	
	centralniPanel.add(tabelaPane);
	centralniPanel.setLayout(new GridLayout(1,1,0,0));

	JPanel juzniPanel = new JPanel();
	JButton modifikujZadatakBtn = new JButton("Modifikuj zadatak");
	modifikujZadatakBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	if(tabela.getSelectedRowCount()>0)
	            	{
	            	indexTabela=tabela.getSelectedRow();
	                ModifikacijaZadatakGUI ex = ModifikacijaZadatakGUI.dajInstancu(controler1.GetZadatak(zadaci, indexTabela));
	                ex.setSize(351, 276);
	                ex.setLocationRelativeTo(null);
	                ex.setVisible(true);
	            	}
	            	else
	            		JOptionPane.showMessageDialog(rootPane,
	    						"Niste odabrali zadatak.",
	    						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
	            		
	            }
	        });
		}
	});
	JButton izbrisiZadatakBtn = new JButton("Izbriši zadatak");
	izbrisiZadatakBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	if(tabela.getSelectedRowCount()>0)
	            	{
	            	indexTabela=tabela.getSelectedRow();
	                IzbrisiZadatakGUI ex = IzbrisiZadatakGUI.dajInstancu(controler1.GetZadatak(zadaci, indexTabela));
	                ex.setSize(600, 150);
	                ex.setLocationRelativeTo(null);
	                ex.setVisible(true);
	            	}
	            	else
	            		JOptionPane.showMessageDialog(rootPane,
	    						"Niste odabrali zadatak.",
	    						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
	            }
	        });
		}
	});
	JButton dodijeliZadatakBtn = new JButton("Dodijeli zadatak");
	dodijeliZadatakBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			 SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	if(tabela.getSelectedRowCount()>0)
		            	{
		            	indexTabela=tabela.getSelectedRow();
		            	RadniZadatak zadatak = new RadniZadatak();
		            	zadatak = controler1.GetZadatak(zadaci, indexTabela);
		            	if(zadatak.getPotpunoDodjeljen()==false)
		            	{
		                OdabirServiseraGUI os = OdabirServiseraGUI.dajInstancu(null,mySelf, zadatak,0);
						os.setVisible(true);
		                os.setTitle("Odabir servisera");
		                os.setSize(1000, 350);
		                os.setLocationRelativeTo(null);		                
		                os.setVisible(true);
		            	}
		            	else
		            		JOptionPane.showMessageDialog(rootPane,
		    						"Radni zadatak je već potpuno dodijeljen.",
		    						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
		            		
		            	}
		            	else
		            		JOptionPane.showMessageDialog(rootPane,
		    						"Niste odabrali zadatak.",
		    						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
		          }
		     });
		}
	});
	JButton prikaziViseBtn = new JButton("Prikaži detaljno");
	prikaziViseBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	if(tabela.getSelectedRowCount()>0)
	            	{
	            	indexTabela=tabela.getSelectedRow();
	                PrikaziDetaljnoZadatakGUI ex = PrikaziDetaljnoZadatakGUI.dajInstancu(controler1.GetZadatak(zadaci, indexTabela), naziviServisera);
	                ex.setSize(400, 400);
	                ex.setTitle("Prikaz radnog zadatka");
	                ex.setLocationRelativeTo(null);
	                ex.setVisible(true);
	            	}
	            	else
	            		JOptionPane.showMessageDialog(rootPane,
	    						"Niste odabrali zadatak.",
	    						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
	            }
	        });
		}
	});
	
	juzniPanel.setBorder(BorderFactory.createEmptyBorder(2, 1, 2, 1));
	juzniPanel.setLayout(new GridLayout(1,6,1,1));
	
	
	juzniPanel.add(prikaziViseBtn);
	juzniPanel.add(modifikujZadatakBtn);
	juzniPanel.add(dodijeliZadatakBtn);
	
	juzniPanel.add(izbrisiZadatakBtn);
	
	getContentPane().add(sjeverniPanel, BorderLayout.NORTH);
	getContentPane().add(centralniPanel, BorderLayout.CENTER);
	getContentPane().add(juzniPanel, BorderLayout.SOUTH);
	


	addWindowFocusListener(new WindowFocusListener() {
		public void windowGainedFocus(WindowEvent arg0) {
			modelTabela.setRowCount(0);
			
			try {
				controler1.PretraziSve();
				if(zadaci.isEmpty()==false) zadaci.clear();
				zadaci.addAll(controler1.getRadniZadaci());
				Ispisi(controler1.getRedovi(controler1.getRadniZadaci()), modelTabela);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(rootPane,
						"Pojavila se greška. Pokušajte ponovo.",
						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
			}
			
			
		}
		public void windowLostFocus(WindowEvent arg0) {
		}
	});
	
	
	datumKreiranjaDP.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			datumKreiranja = (Date) datumKreiranjaDP.getModel().getValue();
		}
	});
	
	datumPruzimanjaDP.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			datumPreuzimanja = (Date) datumPruzimanjaDP.getModel().getValue();
		}
	});

	datumIzvrsenjaDP.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			datumIzvrsenja = (Date) datumIzvrsenjaDP.getModel().getValue();
		}
	});

	datumZavrsniDP.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			krajnjiDatumIzvrsenja = (Date)datumZavrsniDP.getModel().getValue();
		}
	});
	
	imePrezimeTxt.addFocusListener(new FocusAdapter() {
		@Override
		public void focusLost(FocusEvent arg0) {
			String string = imePrezimeTxt.getText();
			String[] naziv = string.split("\\s+");
			
			ime = naziv[0];
			if(naziv.length>1)
				{
					prezime = naziv[1];
					if(naziv.length>2)
						for(int i=2; i<naziv.length; i++) prezime+=" "+naziv[i];
					
				}
		}
	});
	

	nazivKlijentaCmb.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			indexKlijent=nazivKlijentaCmb.getSelectedIndex();
			if(indexKlijent>0) klijentOdabran=true;
		}
	});

	pretraziBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			modelTabela.setRowCount(0); 
			
			if(ime=="" && prezime=="" &&  klijentOdabran==false && datumIzvrsenja==null &&
					datumKreiranja==null && datumPreuzimanja==null &&
					krajnjiDatumIzvrsenja==null && nedodjeljenZadatakChckbx.isSelected()==false
					&& neizvrsenZadatakChckbx.isSelected()==false && neprihvacenZadatakChckbx.isSelected()==false)
			{
				JOptionPane.showMessageDialog(rootPane,
						"Morate odabrati barem jedan kriterij pretrage.",
						"Poruka o grešci", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
			RadniZadaciRacunovodstvoControler controler2 = new RadniZadaciRacunovodstvoControler();
			
			Boolean radniZadaciNadjeni;
			try {
				radniZadaciNadjeni = controler2.PronadjiRadneZadatke(ime, prezime, klijentOdabran,indexKlijent, datumKreiranja, datumPreuzimanja, 
						datumIzvrsenja, krajnjiDatumIzvrsenja, neizvrsenZadatakChckbx.isSelected(), nedodjeljenZadatakChckbx.isSelected(), neprihvacenZadatakChckbx.isSelected());
			
			if(radniZadaciNadjeni)
			{
				if(zadaci.isEmpty()==false)zadaci.clear();
				zadaci.addAll(controler2.getLista());
				Ispisi(controler2.getRedovi(controler2.getLista()), modelTabela);
			}
			else 
			{
				JOptionPane.showMessageDialog(rootPane,
				"Ne postoji radni zadatak u bazi podataka.",
				"Poruka o grešci", JOptionPane.INFORMATION_MESSAGE);
			}
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			ime="";
			prezime="";
			klijentOdabran=false;
			datumIzvrsenja=null;
			datumKreiranja=null; 
			datumPreuzimanja=null;
			krajnjiDatumIzvrsenja=null;
			nedodjeljenZadatakChckbx.setSelected(false);
			neizvrsenZadatakChckbx.setSelected(false);
			neprihvacenZadatakChckbx.setSelected(false);
			datumIzvrsenjaDP.getJFormattedTextField().setText("");
			datumKreiranjaDP.getJFormattedTextField().setText("");
			datumPruzimanjaDP.getJFormattedTextField().setText("");
			datumZavrsniDP.getJFormattedTextField().setText("");
			imePrezimeTxt.setText("");
			}
		}
	});
}

	@SuppressWarnings("rawtypes")
	public void Ispisi(List<List> redoviTabele, DefaultTableModel modelTabela)
	{
		for(int i=0; i<redoviTabele.size(); i++)
		modelTabela.addRow(new Object[]{redoviTabele.get(i).get(0),redoviTabele.get(i).get(1), 
		redoviTabele.get(i).get(2), redoviTabele.get(i).get(3)});
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		unistiInstancu();
		super.dispose();
	}
	
}
