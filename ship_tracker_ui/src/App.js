import { useEffect, useState } from 'react';
import ShipMap from './ShipMap';

function App() {
  const [shipMap, setShipMap] = useState({}); // keyed by mmsi

  useEffect(() => {
    let socket;

    const connect = () => {
      socket = new WebSocket('ws://localhost:8072/ws/entities');

      socket.onopen = () => {
        console.log('🟢 WebSocket connected');
      };

      socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);

          // Handle single object or array of ships
          const newShips = Array.isArray(data) ? data : [data];

          setShipMap((prevMap) => {
            const updatedMap = { ...prevMap };
            newShips.forEach(({ mmsi, lat, lon }) => {
              if (mmsi && lat && lon) {
                updatedMap[mmsi] = { mmsi, lat, lon };
              }
            });
            return updatedMap;
          });
        } catch (err) {
          console.error('❌ JSON parse error:', err.message, event.data);
        }
      };

      socket.onclose = () => {
        console.log('🔴 WebSocket disconnected, retrying...');
        setTimeout(connect, 2000);
      };

      socket.onerror = (err) => {
        console.error('🚨 WebSocket error:', err);
        socket.close();
      };
    };

    connect();

    return () => {
      if (socket && socket.readyState === WebSocket.OPEN) {
        socket.close();
      }
    };
  }, []);

  const ships = Object.values(shipMap); // convert for rendering

  return <ShipMap ships={ships} />;
}

export default App;