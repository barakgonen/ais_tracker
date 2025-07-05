import { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

export default function ShipMap({ ships }) {
  const mapRef = useRef(null);
  const markersRef = useRef([]);

  const shipIcon = new L.Icon({
    iconUrl: '/ship.png',
    iconSize: [32, 32],
    iconAnchor: [16, 16],
    popupAnchor: [0, -16],
  });

  useEffect(() => {
    if (!mapRef.current) {
      mapRef.current = L.map('map').setView([69.42, 18.11], 6);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
      }).addTo(mapRef.current);
    }
  }, []);

  useEffect(() => {
    const map = mapRef.current;
    if (!map) return;

    // Remove old markers
    markersRef.current.forEach(marker => marker.remove());
    markersRef.current = [];

    ships.forEach(({ mmsi, lat, lon }) => {
      if (!lat || !lon || !mmsi) return;

      const marker = L.marker([lat, lon], { icon: shipIcon })
        .addTo(map)
        .bindPopup(`
          <div>
            <strong>Ship ID:</strong> ${mmsi}<br/>
            <a href="https://www.marinevesseltraffic.com/2013/06/mmsi-number-search.html?mmsi=${mmsi}" 
               target="_blank" rel="noopener noreferrer">
              View on MarineVesselTraffic
            </a>
          </div>
        `);

      markersRef.current.push(marker);
    });
  }, [ships]);

  return <div id="map" style={{ height: '100vh', width: '100%' }} />;
}