package com.thirteendollars.guesser.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.thirteendollars.guesser.R;

public class Credits extends AppCompatActivity {

    private String info="-------[ZASADY]\n" +
            " \n" +
            "Zgaduj słowa i zdobywaj monety,które umożliwią Ci kupowanie wyższych poziomów ułatwień.\n" +
            "Wygrywasz osiągając poziom gry 25!\n" +
            "Czy podołasz temu zadaniu?\n" +
            "Będziesz szybszy od innych?\n" +
            "Przekonajmy się!\n" +
            " \n" +
            " \n" +
            "[ZGADUJ:]\n" +
            "Do dyspozycji masz:\n" +
            "-słowa z bazy aplikacji o długości od 3 do 20 liter\n" +
            "-słowa od graczy o długości od 3 do 25 liter\n" +
            " \n" +
            "Słowa z bazy aplikacji:\n" +
            "*Za każdą poprawną literę zyskujesz 1 monetę\n" +
            "*W przypadku wygranej otrzymujesz dodatkowo jeszcze 2 monety za każdą literę oraz (długość_słowa-1)monet za każdą niewykorzystaną próbę zgadywania.\n" +
            "*W przypadku przegranej nic nie tracisz\n" +
            " \n" +
            "Słowa od graczy:\n" +
            "*Za każdą poprawną literę zyskujesz 10 monet\n" +
            "**W przypadku wygranej otrzymujesz dodatkowo jeszcze (5*długość_słowa)monet za każdą literę oraz (długość_słowa-1)monet za każdą niewykorzystaną próbę zgadywania.\n" +
            "*W przypadku przegranej tracisz (długość_słowa)monet za każdą literę\n" +
            " \n" +
            "[ULEPSZAJ:]\n" +
            "Przechodząc do ulepszeń możesz ulepszyć odpowiednio:\n\n" +
            "PRÓBY - zwiększyć ilość możliwych prób wpisywania liter\n" +
            "CZAS - zwiększyć dostępny czas na rozpracowanie słowa\n" +
            "LITERY-zwiększyć ilość liter które dostajesz na start\n" +
            "POZIOM GRY - zwiększyć maksymalną długość słów jaką możesz zgadywać,\n" +
            "zwiększyć maksymalną ilość słów które możesz stworzyć\n" +
            " \n" +
            "[TWÓRZ:]\n" +
            "Jeżeli uzyskasz poziom gry 4,możesz zacząć dodawać własne słowa.\n" +
            "Dodając własne słowa nic nie tracisz,a inni użytkownicy mogą je zgadywać.\n" +
            "Postaraj się - za każdą przegraną gracza,spowodowaną Twoim słowie otrzymujesz premię 250 monet.\n" +
            "Możesz je przelać na konto główne w każdej chwili i ulepszać szybciej.\n" +
            " \n" +
            "[WYGRYWAJ]\n" +
            "Bądź pierwszy w rankingu!\n"+
            "O miejscu decydują punkty procentowe -obliczane na podstawie poziomu gry,pokazują postęp gracza. \n" +
            "Liczba pod wskaźnikiem procentowym,to ilość własnych słów gracza - przepis na wygraną?\n" +
            " \n" +
            "----------------------------------\n" +
            "Wszelkie uwagi,spostrzeżenia i krytykę można kierować bezpośrednio na maila podanego poniżej.\n" +
            "Powodzenia!\n\n" +
            "Kontakt: thirteendollars.com@gmail.com\n"+
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n"+
            "\n" +
            "Muzyka w tle:\n" +
            "AUTHOR: Orkha,MUSIC: RBH part II\n" +
            "AUTHOR: ELVD,MUSIC: Undisturbed\n" +
            "AUTHOR: BoxCat,MUSIC: Battle End\n" +
            "AUTHOR: BoxCat,MUSIC: Ambient Gaming Background\n" +
            "AUTHOR: Jacob LaFlash,MUSIC: Outro\n" +
            "AUTHOR: Andrew,MUSIC: Never Go Back\n" +
            "AUTHOR: Iman Design,MUSIC: Kick Back\n" +
            "AUTHOR: RoyalW,MUSIC: IM COMIN BACK!\n" +
            "AUTHOR: Charles Lennon-i0n_bl4d3,MUSIC: Drive-by suicide\n" +
            "AUTHOR: Charles Lennon-i0n_bl4d3,MUSIC: Orient Intent\n" +
            "\n" +
            "Efekty dźwiękowe:\n" +
            "AUTHOR:jonas,MUSIC: dr dre\n" +
            "AUTHOR:Intermedia Design Graphics,MUSIC: beep\n" +
            "AUTHOR:socreative.tv,MUSIC: Button Beep Sound\n" +
            "AUTHOR:public domain,MUSIC: Cash register\n" +
            "\n" +
            "Wszytskie ścieżki dźwiękowe zostały pobrane z www.flashkit.com,na licencji freeware.\n";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        ((TextView)findViewById(R.id.credits_textview)).setText(info);

        try{
            getSupportActionBar().hide();
        }
        catch(NullPointerException actionBarException){
            Toast.makeText(getApplicationContext(), R.string.actionBarException, Toast.LENGTH_SHORT).show();
        }
        getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
    }


        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
                super.onWindowFocusChanged(hasFocus);
                if (hasFocus) getWindow().getDecorView().setSystemUiVisibility(AppStaticData.FLAGS);
        }


}
