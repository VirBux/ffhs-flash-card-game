// Utils
const toast = new Toast();

// Services
const playerService = new PlayerService();
const gameService = new GameService();
const flashCardService = new FlashCardService();
const chatService = new ChatService();

// Provice other services with SocketService
const socketService = new SocketService();
playerService.addSocketService(socketService);
gameService.addSocketService(socketService);
flashCardService.addSocketService(socketService);
chatService.addSocketService(socketService);

// Components
const game = new GameComponent();
