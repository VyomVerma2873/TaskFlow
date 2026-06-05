import { useState, useEffect } from 'react';
import { Shield, ShieldCheck, ShieldAlert, Loader2, Link2 } from 'lucide-react';
import api from '../services/api';
import Navbar from '../components/Navbar';

export default function BlockchainPage() {
  const [chain, setChain] = useState([]);
  const [valid, setValid] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [chainRes, validRes] = await Promise.all([
        api.get('/blockchain/chain'),
        api.get('/blockchain/validate'),
      ]);
      setChain(chainRes.data);
      setValid(validRes.data.valid);
    } catch (err) {
      console.error('Failed to load blockchain data', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />

      <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 mb-8">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
              <Link2 className="w-6 h-6 text-indigo-600" />
              Blockchain Ledger
            </h1>
            <p className="text-slate-500 mt-1 text-sm">
              Immutable audit trail of all task events
            </p>
          </div>

          {valid !== null && (
            <button
              onClick={fetchData}
              className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition ${
                valid
                  ? 'bg-green-50 text-green-700 hover:bg-green-100'
                  : 'bg-red-50 text-red-700 hover:bg-red-100'
              }`}
            >
              {valid ? (
                <ShieldCheck className="w-4 h-4" />
              ) : (
                <ShieldAlert className="w-4 h-4" />
              )}
              {valid ? 'Chain Valid' : 'Chain Compromised'}
            </button>
          )}
        </div>

        {loading ? (
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-8 h-8 text-indigo-600 animate-spin" />
          </div>
        ) : chain.length === 0 ? (
          <div className="text-center py-20">
            <Shield className="w-16 h-16 text-slate-300 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-slate-600">No blockchain data yet</h3>
            <p className="text-slate-400">Blocks will appear here as tasks are created and modified.</p>
          </div>
        ) : (
          <div className="space-y-4">
            {chain.map((block) => (
              <div
                key={block.index}
                className="bg-white rounded-xl border border-slate-200 p-5 hover:shadow-md transition"
              >
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-3">
                    <span className="inline-flex items-center justify-center w-8 h-8 rounded-lg bg-indigo-100 text-indigo-700 text-sm font-bold">
                      #{block.index}
                    </span>
                    <span className="text-sm text-slate-500">
                      {new Date(block.timestamp).toLocaleString()}
                    </span>
                  </div>
                </div>

                <div className="space-y-2 text-sm">
                  <div>
                    <span className="font-medium text-slate-600">Data: </span>
                    <code className="bg-slate-50 px-2 py-0.5 rounded text-xs text-slate-700 break-all">
                      {block.data}
                    </code>
                  </div>
                  <div>
                    <span className="font-medium text-slate-600">Hash: </span>
                    <code className="bg-slate-50 px-2 py-0.5 rounded text-xs text-green-700 break-all">
                      {block.hash}
                    </code>
                  </div>
                  <div>
                    <span className="font-medium text-slate-600">Prev Hash: </span>
                    <code className="bg-slate-50 px-2 py-0.5 rounded text-xs text-slate-500 break-all">
                      {block.previousHash}
                    </code>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
