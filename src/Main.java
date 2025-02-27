import model.Song;
import service.MP3PlayerImpl;
import service.Mp3Player;

import java.util.Scanner;


public class Main {

    private static Mp3Player player = new MP3PlayerImpl();

        static void showoptions() {

            System.out.println("Zgjidh opsionin:");
            System.out.println("1. Shfaq playlist");
            System.out.println("2. Play");
            System.out.println("3. Stop");
            System.out.println("4. Next");
            System.out.println("5. Shto kenge");
            System.out.println("6. Hiq kenge");
            System.out.println("7. Play first");
            System.out.println("8. Play last");
            System.out.println("9. Search");
            System.out.println("10. Dil");
        }


        public static void main(String[] args) {
            player.loadSong();
            Scanner scanner = new Scanner(System.in);

            String action = "";
            Main.showoptions();

            while (!action.equals("10")) {

                action = scanner.next();


                switch (action) {
                    case ("1"):
                        player.showplaylist();
                        break;

                    case ("2"):
                        System.out.println("Currently playing " + player.getCurrentSong());
                        break;

                    case ("3"):
                        player.stopSong();
                        break;

                    case ("4"):
                        if(player.listSongs().isEmpty()){
                            System.out.println("Playlisti eshte bosh");
                        }else{
                            Song a = player.playNext();
                            System.out.println("Now playing: " + a.getEmri() + " by " + a.getArtist());
                        }
                        break;

                    case ("5"):

                        System.out.println("Emri:");
                        scanner.nextLine();
                        String titulli = scanner.nextLine();

                        System.out.println("Artisti:");
                        String emriArtisti = scanner.nextLine();

                        System.out.println("Path:");
                        String Path = scanner.nextLine();


                        Song kengERe = new Song(titulli,emriArtisti);
                        kengERe.setPath(Path);
                        player.addsong(kengERe);
                        player.showplaylist();

                        break;


                    case ("6"):
                        player.showplaylist();
                        System.out.println("Zgjidhni numrin e kenges qe deshironi te hiqni");
                        int hiq = scanner.nextInt();
                        player.removesong(hiq-1);
                        player.showplaylist();
                        break;

                    case ("7"):
                        if(player.listSongs().isEmpty()){
                            System.out.println("Playlisti eshte bosh");
                        }else{
                            Song a = player.playFirst();
                            System.out.println("Now playing: " + a.getEmri() + " by " + a.getArtist());
                        }
                        break;


                    case ("8"):
                        if(player.listSongs().isEmpty()){
                            System.out.println("Playlisti eshte bosh");
                        }else{
                            Song a = player.playLast();;
                            System.out.println("Now playing: " + a.getEmri() + " by " + a.getArtist());
                        }
                        break;


                    case ("9"):
                        System.out.println("Fusni titullin");
                        String searchTitle = scanner.next();
                        Song kengaKerkuar = player.findByTitle(searchTitle);

                        if (kengaKerkuar != null) {
                            System.out.println("Search results: " + kengaKerkuar.getEmri() + " by " + kengaKerkuar.getArtist());
                        } else {
                            System.out.println("No results found");
                        }
                        break;

                    case ("10"):
                        player.saveSongOnExit();
                        System.exit(0);
                        break;

                        default:
                        System.out.println("Incorrect input!");


                }
            }

        }
    }