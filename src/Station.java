import java.util.ArrayDeque;
import java.util.Deque;


public class Station{
	Integer name;
	double Avarage;
	Deque<Double> Q =new ArrayDeque<Double>();
	double sum;
	
	public Station(int a) {
		name = a;
	}
	public void printnr() {
		System.out.println(name);
	} 
 
	public void Temp_queue_aanpassen(double new_double) {
		Q.remove();
		Q.add(new_double);
	}
	
	//deze rekent voortschrijdend gemiddelde op basis van de temperaturen in de queue.
	public double Avarage() {
		double som = 0;
		for(double waarde: Q) {
			som = som += waarde;
		}
		Avarage= som / Q.size();
		return Avarage;
	}
	
	public double Extrapolate() {
		//Iterator<double> iterator = Q.descendingIterator();
		double somx = 0;
		double somy = 0;
		double somxy = 0;
		double somx2 = 0;
		double gemx = 0;
		double gemy=0;
		int n = Q.size();
		double a =1;
		double Richtingscoef = 0;
		double Constante = 0;
		double Volgende_schatting = 0;
		
		//somxy & somy & gemy
		//while(iterator.hasNext()){
			//double waarde = iterator.next();
		for(double waarde: Q) {
			//System.out.println("alle y waardes en x:"+a+ "-->"+ waarde );
			double som = a * waarde;
			a +=1;
			somxy += som;
			somy += waarde;
			gemy = somy/n;
		}
		//somx^2 & somx & gemx
		for(int i =0; i<n ;i++ ) {
			 somx2 += Math.pow((i+1),2);
			 somx += (i+1);
			 gemx = somx/n;
		}
		
		/*
		System.out.println("somx:"+somx +"somx2:"+somx2+"gemx:"+gemx);
		System.out.println("somy:"+somy+"gemy:"+gemy + "somxy"+ somxy);
		*/
		Richtingscoef =((n * somxy) - (somx * somy))/ ((n * somx2) - (Math.pow(somx, 2)));
		Constante = gemy - Richtingscoef*gemx;
		Volgende_schatting = Constante + Richtingscoef*(n+1); 
		System.out.println(a);
		System.out.println("RC:"+ Richtingscoef +" Constante:"+ Constante);
		System.out.println("Volgendewaarde:"+ Volgende_schatting);
		
		
		return Volgende_schatting;
	}

}