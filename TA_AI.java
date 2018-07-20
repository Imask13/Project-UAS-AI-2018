/*
	Program Naive Bayes
Masalah: - Mengklasifikasikan apakah pelanggan akan membeli laptop atau tidak tergantung pada data dalam set tes.
Probabilitas dihitung untuk membeli dan tidak membeli casing dan prediksi yang sesuai dibuat.
Set tes memiliki kolom berikut di mana prediksi dilakukan: 
1) usia: (pemuda / tengah / senior)
2) penghasilan: (rendah / sedang / tinggi)
3) siswa: (ya / tidak)
4) credit_rating: (cukup / bagus)
5) kelas: (ya / tidak)
Tes ditetapkan dengan kolom di atas dan nilai kolom dari opsi di braket untuk disimpan dalam basis data
(Untuk program ecompany adalah nama yang diberikan ke database)
kelas memberitahu pelanggan dalam set tes telah membeli laptop atau tidak.
Ketika 4 nilai bidang pertama diberikan sebagai program input membuat prediksi untuk ya (akan membeli)
atau tidak (tidak akan membeli).
	
*/

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class NaiveBayes
{
    public static void main(String args[])
    {
		try
		{
			/*
			k1 dan k2 menunjukkan kelas 1 & kelas 2.
                        kelas1: - Dia membeli laptop
                        kelas2: - Dia tidak membeli laptop
			*/
                        String url ="jdbc:mysql://localhost/bayes";
                          Connection koneksi = null;
        try {
            koneksi = (Connection) DriverManager.getConnection(url,"root", "");
        } catch (SQLException ex) {
            Logger.getLogger(NaiveBayes.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
			Statement s = koneksi.createStatement();

			String query = null;
			ResultSet rs = null;
			int k1=0 ,k2=0 ,n=0;

			query ="SELECT COUNT(*) FROM laptop WHERE Kelas = 'Ya'";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					// Jumlah kasus saat laptop dibeli dan set pelatihan
					k1=Integer.parseInt(rs.getString(1));
                        }
			query ="SELECT COUNT(*) FROM  laptop where Kelas = 'Tidak' ";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					// Jumlah kasus ketika laptop tidak dibeli di set pelatihan
					k2=Integer.parseInt(rs.getString(1)); 
                        }
			query = "SELECT COUNT(*) FROM  laptop ";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					//Count of total cases in training set
					n = Integer.parseInt(rs.getString(1)); 
                        }
			float pc1 = (float)k1/n; // Probabilitas umum untuk kelas k1
			float pc2 = (float)k2/n; // Probabilitas umum untuk kelas k2
                        
                        String usia, pendapatan, siswa, credit_rating, kelas1;
                        Scanner sc = new Scanner(System.in);
			
			// Terima nilai parameter untuk kelas mana yang diprediksi
			
			System.out.println("Masukkan Usia : (Muda / Tengah / Senior)");
			usia = sc.next();

			System.out.println("Masukkan Pendapatan : (Rendah / Sedang / Tinggi)");
			pendapatan = sc.next();

			System.out.println("Masukkan Siswa : (Ya / Tidak)");
			siswa = sc.next();

			System.out.println("Masukkan Credit_Rating : (Cukup / Bagus)");
			credit_rating = sc.next();
                        
                        System.out.println("Kelas : (Ya / Tidak)");
			kelas1 = sc.next();


			float pinc1=0,pinc2=0;
			// pinc1 = Prediksi menjadi kelas1 (akan membeli)
                        // pinc2 = Prediksi menjadi kelas2 (tidak akan membeli)
				
			pinc1 = pfind(usia, pendapatan, siswa, credit_rating, "Ya");
			pinc2 = pfind(usia, pendapatan, siswa, credit_rating, "Tidak");

			pinc1 = pinc1 * pc1;
			pinc2 = pinc2 * pc2;

			// bandingkan pinc1 & pinc2 dan memprediksi kelas yang akan atau tidak ingin dibeli oleh pengguna
			if(pinc1 > pinc2){
				System.out.println("Dia akan membeli laptop");
                        }else{
				System.out.println("Dia tidak akan membeli laptop");
                        }
			s.close();
			
		}
		catch(Exception e)
		{
			System.out.println("Exception:"+ e);
		}
	}

	public static float pfind(String usia,String pendapatan,String siswa,String credit_rating,String kelas1)
	{
		float ans = 0;
		try{
			Scanner sc = new Scanner(System.in);
                        String url ="jdbc:mysql://localhost/bayes";
                        String user="root";
                        String pass="";
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:odbc:ecompany");
			Statement s = con.createStatement();
			String query = null;
			ResultSet rs = null;
			int a=0 , b=0 , c=0 , d=0 , total=0;
			
			/* 	Kueri di bawah ini disusun menggunakan nilai parameter usia, pendapatan, siswa, credit_rating, kelas
diteruskan ke fungsi. Fungsi menemukan kemungkinan untuk setiap parameter individual dari nilai kelas yang disediakan
dan menggunakan teorema naive bayes itu menghitung probabilitas total */
			

			query ="SELECT COUNT(*) FROM  laptop WHERE   (Usia = '"+ usia + "' ) AND (Kelas = '" +kelas1 +"') ";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					a=Integer.parseInt(rs.getString(1));
			// a = hitungan nilai dalam set pelatihan yang memiliki usia, kelas yang sama dengan yang dilewatkan dalam argumen
                        }
			query ="SELECT COUNT(*) FROM  laptop WHERE   ( Pendapatan = '"+ pendapatan + "' ) AND (Kelas = '" +kelas1 +"') ";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					b=Integer.parseInt(rs.getString(1));
			// b = hitungan nilai dalam set pelatihan yang memiliki pendapatan, kelas yang sama seperti yang dilewatkan dalam argumen
                        }

			query ="SELECT COUNT(*) FROM laptop WHERE ( Siswa = '"+ siswa + "' ) AND (Kelas = '" +kelas1 +"') ";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					c=Integer.parseInt(rs.getString(1));
			// c = hitungan nilai dalam set pelatihan yang memiliki siswa, kelas yang sama seperti yang dilewatkan dalam argumen
                        }

			query ="SELECT COUNT(*) FROM  laptop WHERE   ( Credit_Rating = '"+ credit_rating + "' ) AND (Kelas = '" +kelas1 +"')";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					d=Integer.parseInt(rs.getString(1)); 
			// d = hitungan nilai dalam set pelatihan yang memiliki credit_rating, kelas yang sama dengan yang dilewatkan dalam argumen
                        }
                        query ="SELECT COUNT(*) FROM laptop WHERE (Kelas = '" +kelas1 +"') ";
			s.execute(query);
			rs= s.getResultSet();
			if(rs.next()){
					total=Integer.parseInt(rs.getString(1)); // total tidak ada hasil

			ans = (float)a / (float)total  * (float)b /(float)total * (float)c /(float)total * (float)d /(float)total ;
			// menghitung probabilitas total dengan naive bayes
                        }
			s.close();
			con.close();
		}catch(Exception e)
                    
		{
		}
		return ans;
	}
}