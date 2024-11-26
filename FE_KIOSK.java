//import javax.management.timer.Timer;
import javax.swing.*;
import java.awt.event.*;
import java.net.http.HttpResponse;
import java.awt.*;
import java.util.*;
import java.util.List;
import com.google.gson.*;// json 데이터 저장방식 변경 ########################################
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;
import java.net.URL;



public class FE_KIOSK extends JFrame {
    //전역 변수 사용
    private int table_num = 1;
    private Map<String, List<Menu>> menuData = new LinkedHashMap<>();
    private Container c;
    private Container left_c;
    private Container mid_c;
    private Container right_c;
    private JPanel leftly_main;
    private JPanel leftly;
    private JPanel midly_main;
    private JPanel midly;
    private JScrollPane midly_menu;
    private JPanel rightly_main;
    private JPanel rightly;
    private JPanel rightly_down;

    // 카테고리 메뉴별 데이터(카테고리정보, 메뉴정보 포함)
    //private Timer timer; //

    public FE_KIOSK(){
        int height = 1000;
        int width = 500;
        setTitle("키오스크");
        setSize(height, width);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 창 크기 지정 3개 컨테이너
        int left_width = (int) (width * 0.3);
        int mid_width = (int) (width * 0.3);
        int right_width = (int) (width * 0.3);
        

        // 전체 화면 컨테이너
        c = getContentPane();
        //c.setLayout(new FlowLayout());
        //c.setLayout(new GridLayout(0, 3, 10, 10));
        c.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // 컴포넌트가 공간을 가득 채우도록 설정
        gbc.weighty = 1.0; // 세로 크기를 동일하게 설정



        
        left_c = new JPanel(); // 각 왼 중 오 컨테이너를 패널로 바꾸면 조절 가능
        left_c.setLayout(new BoxLayout(left_c, BoxLayout.Y_AXIS));
        left_c.setBounds(0, 0, left_width, height);

        mid_c = new JPanel();
        mid_c.setLayout(new BoxLayout(mid_c, BoxLayout.Y_AXIS));
        mid_c.setBounds(left_width, 0, mid_width, height);

        right_c = new JPanel();
        right_c.setLayout(new BoxLayout(right_c, BoxLayout.Y_AXIS));
        right_c.setBounds(left_width + mid_width, 0, right_width, height);

        //mid_c.setMaximumSize(new Dimension(Short.MAX_VALUE, mid_c.getPreferredSize().height));//????

        // 카테고리부분 왼쪽
        leftly_main = new JPanel();
        leftly_main.setLayout(new BoxLayout(leftly_main, BoxLayout.Y_AXIS));
        leftly = new JPanel();
        leftly.setLayout(new BoxLayout(leftly, BoxLayout.Y_AXIS)); // basic_load(leftly_main);
        basic_load(leftly_main);
        conn_be();
        
        // 메뉴 보이는 부분 중간
        midly_main = new JPanel();
        midly_main.setLayout(new BoxLayout(midly_main, BoxLayout.Y_AXIS));
        //midly_main.setPreferredSize(new Dimension(100000000, 10));// 여기 추가했음 확인##################
        midly = new JPanel();
        //midly.setLayout(new BoxLayout(midly, BoxLayout.Y_AXIS));
        //midly.setLayout(new GridLayout(0, 3, 10, 10));
        midly.setLayout(new GridBagLayout());
        midly.setPreferredSize(new Dimension(midly.getPreferredSize().width, 500));

        midly_menu = new JScrollPane(midly);
        midly_menu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //midly_menu.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //midly_menu.setPreferredSize(new Dimension(20000000, 400000));
        //midly_menu.setLayout(new FlowLayout());

        // 계산 하는 부분 오른쪽
        rightly_main = new JPanel(); // 레이아웃 미정
        rightly = new JPanel();
        rightly.setLayout(new BoxLayout(midly, BoxLayout.Y_AXIS));
        rightly_down = new JPanel();
        
        // 
        /*추가시 
         * leftly.add(추가소스); 시도
         */
        left_c.add(leftly_main);
        left_c.add(leftly);

        mid_c.add(midly_main);
        mid_c.add(midly_menu);

/* 
        // 컨테이너 창 늘리는 기능
        left_c.setBounds(0, 0, left_width, width);
        mid_c.setBounds(left_width, 0, mid_width, width);
        right_c.setBounds(left_width + mid_width, 0, right_width, height);
        */
        gbc.gridx = 0;
        gbc.weightx = 0.15; // 30% 가로 비율
        c.add(left_c, gbc);
        //c.add(left_c);

        gbc.gridx = 1;
        gbc.weightx = 0.5; // 40% 가로 비율
        c.add(mid_c, gbc);
        //c.add(mid_c);

        gbc.gridx = 2;
        gbc.weightx = 0.35; // 30% 가로 비율
        c.add(right_c, gbc);
        //c.add(right_c);

        // 기본 데이터 로드
        load_data();

        // 카테고리 버튼 추가
        //addcategorybt(leftly);

        setVisible(true);
    }

    private void show_menu(String category){
        midly.removeAll();

        List<Menu> menuList = menuData.get(category);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 간격
        //gbc.fill = GridBagConstraints.BOTH; // 버튼 크기를 고정
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // 가로로 균등 배치
        gbc.weighty = 1;

        for (int i = 0 ; i < menuList.size() ; i++){ 
            Menu menu = menuList.get(i);

            try{
                System.out.println(menu.imageUrl);
                URL url = new URL(menu.imageUrl);
                JButton menubt = new JButton(
                    "<html>" + 
                    "<img src='" + url + "' width='100' height='100'>" + "<br>" +
                    //menu.imageUrl + "<br>" +
                    menu.name + "<br>" + 
                    menu.price + "<br>" + 
                    "</html>");
                menubt.addActionListener(e -> {
                    // 클릭된 메뉴에 대한 처리를 여기에 추가
                    System.out.println("클릭된 메뉴: " + menu.name);
                    // 예를 들어, 해당 메뉴의 상세 정보를 보여주거나 다른 작업을 할 수 있습니다.
                });
                menubt.setEnabled(menu.available);
                menubt.setPreferredSize(new Dimension(200, 100));
                
                gbc.gridx = i % 3; // 열 위치
                gbc.gridy = i / 3; // 행 위치
                midly.add(menubt, gbc);

            } catch (MalformedURLException e) {
                System.err.println("잘못된 URL 형식: " + menu.imageUrl);
                e.printStackTrace();  // 예외 출력
            } catch (Exception e) {
                System.err.println("URL 생성 중 예외 발생: " + menu.imageUrl);
                e.printStackTrace();  // 다른 예외 출력
            }

        }

        midly.revalidate();
        midly.repaint();
        mid_c.revalidate();
        mid_c.repaint();
    }

    public void load_data(){// 수정했을때 정상작동함 (id값 상관없이 데이터만 함) 데이터 분리 필요한것만 위치시킴
        
    }

    public void conn_be(){ // 백엔드 연결 나중에 추가필요 #####################################################(postman)
        // menudata_get
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("https://be-api-takaaaans-projects.vercel.app/api/category")
                //.url("https://be-api-takaaaans-projects.vercel.app/api/menu?available=true&category=main-dish")
                .url("https://be-api-takaaaans-projects.vercel.app/api/menu?available=true")
                .build();

        try (Response response = client.newCall(request).execute()) {
            // 서버 응답 받은 JSON 문자열
            String responseBody = response.body().string();
            System.out.println(responseBody);
            JsonObject jsonResponse = new JsonParser().parse(responseBody).getAsJsonObject();
            JsonArray menuItemsArray = jsonResponse.getAsJsonArray("menuItems");

            // 메뉴 항목 처리
            for (JsonElement element : menuItemsArray) {
                JsonObject menuItemObj = element.getAsJsonObject();
                String id = menuItemObj.get("_id").getAsString();
                String name = menuItemObj.get("name").getAsString();
                int price = menuItemObj.get("price").getAsInt();
                String imageUrl = menuItemObj.get("imageUrl").getAsString();
                boolean available = menuItemObj.get("available").getAsBoolean();
                String category = menuItemObj.get("category").getAsString();

                // Menu 객체 생성
                Menu menu = new Menu(id, name, price, imageUrl, available, category);

                // 카테고리별 메뉴 리스트에 추가
                menuData.putIfAbsent(category, new ArrayList<>());
                menuData.get(category).add(menu);
            }

            // 카테고리별 메뉴 데이터 출력 테스트용
            System.out.println("카테고리별 메뉴 데이터:");
            for (String category : menuData.keySet()) {
                System.out.println("카테고리: " + category);
                for (Menu item : menuData.get(category)) {
                    // 직접 item.name과 item.price로 접근
                    System.out.println("  메뉴: " + item.name + ", 가격: " + item.price);
                }
            }
            
            // 데이터 불러올 시 데이터 추가
            addcategorybt(leftly);
        } catch (Exception e) {
            e.printStackTrace();
        }     
    }

    //카테고리 leftly에 동적 추가하는 부분 (처음에 leftly 부분 초기화후 새로고침시 다시 작동하게 하면 동적 수정 가능)
    private void addcategorybt(JPanel leftly){
        leftly.removeAll();
        for (String category : menuData.keySet()) {
            JButton categoryButton = new JButton(category);
            categoryButton.addActionListener(e -> {
                System.out.println("카테고리 버튼 클릭: " + category);
                // 카테고리 버튼 클릭시 로직
                show_menu(category);
            });
            leftly.add(categoryButton);
        }
    
        leftly.revalidate();
        leftly.repaint();
        left_c.revalidate();
        left_c.repaint();
    }
    // 여기 html 태그 사용가능 css도 가능하지만 부분적으로 불가능할 수 있다고 함 ..(확인필요)
    // 가운데 카테고리 버튼 클릭시 그 카테고리 정보 출력 JLabel로 나중에 사진 추가 가능



    private void handleMenuClick(){ //메뉴 클릭시 하는거
        System.out.println("hi"); // 추가 필요########################################################
    }

    // menu class
    static class Menu{
        private String id;
        private String name;
        int price;
        String imageUrl;
        boolean available;
        String category;

        public Menu(String id, String name, int price, String imageUrl, boolean available, String category){
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.available = available;
            this.category = category;
        }
    }

    private void rotate_data(){ // time 설정 해야함 모르겠음 ;;
        
    }

    private void basic_load(JPanel leftly_main){
        Date currentDate = new Date();
        
        // 날짜 포맷 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MM - dd");
        
        // 날짜 형식에 맞춰서 문자열로 변환
        String formattedDate = dateFormat.format(currentDate);

        // 날짜를 JLabel로 표시
        JLabel dateLabel = new JLabel(formattedDate);

        // leftly_main 패널에 날짜 레이블 추가
        leftly_main.add(dateLabel);

        // 레이아웃 갱신
        leftly_main.revalidate();
        leftly_main.repaint();
    }

    public static void main(String[] args){
        FE_KIOSK kiosk = new FE_KIOSK();
        //kiosk.conn_be();  
    }   
}