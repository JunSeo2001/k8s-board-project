import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import BoardList from './components/BoardList';
import BoardDetail from './components/BoardDetail';
import BoardForm from './components/BoardForm';
import './App.css';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <div className="app">
          <Routes>
            <Route path="/" element={<BoardList />} />
            <Route path="/boards/new" element={<BoardForm />} />
            <Route path="/boards/:id" element={<BoardDetail />} />
            <Route path="/boards/:id/edit" element={<BoardForm />} />
          </Routes>
        </div>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;






