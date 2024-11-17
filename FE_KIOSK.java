import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class FE_KIOSK extends JFrame {
    //전역 변수 사용
    private int table_num;
    private Map<String, List<Menu>> menuData = new HashMap<>();
    private Container c;
    private JPanel leftly;
    private JPanel midly;
    private JPanel rightly;


    public FE_KIOSK(){
        setTitle("키오스크");
        setSize(500, 500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 전체 화면 컨테이너
        c = getContentPane();
        c.setLayout(new FlowLayout());
        // 카테고리부분 왼쪽
        leftly = new JPanel();
        leftly.setLayout(new BoxLayout(leftly, BoxLayout.Y_AXIS));
        // 메뉴 보이는 부분 중간
        midly = new JPanel();
        midly.setLayout(new BoxLayout(midly, BoxLayout.Y_AXIS));
        // 계산 하는 부분 오른쪽
        rightly = new JPanel();
        rightly.setLayout(new BoxLayout(midly, BoxLayout.Y_AXIS));
        
        // 
        /*추가시 
         * leftly.add(추가소스); 시도
         */
        c.add(leftly);
        c.add(midly);

        // 기본 데이터 로드
        load_data();

        // 카테고리 버튼 추가
        addcategorybt(leftly);

        setVisible(true);
    }

    public void load_data(){// 수정했을때 정상작동함 (id값 상관없이 데이터만 함) 데이터 분리 필요한것만 위치시킴
        String jsonResponse = """ 
            [
                { "id": 1, "category": "main menu", "name": "물", "price": 100 },
                { "id": 2, "category": "main menu", "name": "콜라", "price": 1500 },
                { "id": 3, "category": "HOT", "name": "아이스크림", "price": 3000 },
                { "id": 4, "category": "dessert", "name": "케이크", "price": 5000 },
                { "id": 6, "category": "HOT", "name": "제육", "price": 1000000 }
            ]
        """;//이런식으로 데이터 나옴

        jsonResponse = jsonResponse.replace("[", "").replace("]", "").trim(); // 배열 부분 제거
        String[] items = jsonResponse.split("},"); // 아이템을 구분

        for (String item : items) {
            item = item.replace("{", "").replace("}", "").trim(); // 각 JSON 객체의 {} 제거
            String[] fields = item.split(","); // 각 필드를 구분

            String name = "";
            int price = 0;
            String category = "";

            // 각 필드를 원하는 데이터로
            for (String field : fields) {
                String[] keyValue = field.split(":");
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");

                switch (key) {
                    case "name":
                        name = value;
                        break;
                    case "price":
                        price = Integer.parseInt(value);
                        break;
                    case "category":
                        category = value;
                        break;
                }
            }

            Menu menu = new Menu(name, price);

            if (!menuData.containsKey(category)){
                menuData.put(category, new ArrayList<>());
            }
            menuData.get(category).add(menu);
        }
    }

    public void conn_be(){ // 백엔드 연결 나중에 추가필요 #####################################################
        
    }

    class Menu { // 메뉴 클래스 (정렬 후 변수 저장위해 필요)
        private String name;
        private int price;

        public Menu(String name, int price){
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }
    }
    //카테고리 leftly에 동적 추가하는 부분 (처음에 leftly 부분 초기화후 새로고침시 다시 작동하게 하면 동적 수정 가능)
    private void addcategorybt(JPanel leftly){
        for (String category : menuData.keySet()){
            JButton categorybt = new JButton(category);
            categorybt.addActionListener(e -> updateMidly(category));
            leftly.add(categorybt);
        }
    }
    // 여기 html 태그 사용가능 css도 가능하지만 부분적으로 불가능할 수 있다고 함 ..(확인필요)
    // 가운데 카테고리 버튼 클릭시 그 카테고리 정보 출력 JLabel로 나중에 사진 추가 가능
    private void updateMidly(String category){
        midly.removeAll();
        List<Menu> menus = menuData.get(category);
        for (Menu menu : menus) { 
            JLabel menuLabel = new JLabel("<html>" + menu.getName() + "<br>" + menu.getPrice() + "원</html>");
            menuLabel.addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    handleMenuClick(menu); // 메뉴 클릭 시 이벤트 처리
                }
    
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
            });
            midly.add(menuLabel);
        }
        midly.revalidate();
        midly.repaint();
    }
    
    private void handleMenuClick(Menu menu){
        System.out.println("hi"); // 추가 필요########################################################
    }

    public static void main(String[] args){
        
        new FE_KIOSK();
    }   
}