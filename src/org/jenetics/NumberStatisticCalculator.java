/* 
 * NumberStatisticCalculator.java, 27.08.2008
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics;

import java.util.List;

import javolution.context.StackContext;

import org.jscience.mathematics.number.Number;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: NumberStatisticCalculator.java,v 1.2 2008-08-28 21:21:13 fwilhelm Exp $
 */
class NumberStatisticCalculator<G extends Gene<?>, N extends Number<N>>
	extends StatisticCalculator<G, N>
{

	public NumberStatisticCalculator() {
	}
	
	@Override
	public NumberStatistic<G, N> evaluate(final List<? extends Phenotype<G, N>> population) {
		final Statistic<G, N> s = super.evaluate(population);
		final int size = population.size();
		
		N fitnessSum = null;
		N fitnessSquareSum = null;
		if (size > 0) {
			fitnessSum = population.get(0).getFitness();
			fitnessSquareSum = fitnessSum.times(fitnessSum);
		}

		StackContext.enter();
		try {
			for (int i = 1; i < size; ++i) {
				final Phenotype<G, N> phenotype = population.get(i); 

				N fitness = phenotype.getFitness();
				fitnessSum = fitnessSum.plus(fitness);
				fitnessSquareSum = fitnessSquareSum.plus(fitness.times(fitness));
			}
			
		} finally {
			StackContext.exit();
		}
		
		return new NumberStatistic<G, N>(s, null, null);
	}
	
	static double torben(double m[]){
		int i;
		int less;
		int greater;
		int equal;
		double min = m[0];
		double max = m[0];
		double guess;
		double maxltguess;
		double mingtguess;
		
		for (i = 1 ; i < m.length; i++) {
			if (m[i] < min) {
				min = m[i];
			}
			if (m[i] > max) {
				max = m[i];
			}
		}
		
		while (true) {
			guess = (min+max)/2;
			less = 0; 
			greater = 0; 
			equal = 0;
			maxltguess = min;
			mingtguess = max;
			
			for (i = 0; i < m.length; i++) {
				if (m[i] < guess) {
					less++;
					if (m[i] > maxltguess) {
						maxltguess = m[i];
					}
				} else if (m[i] > guess) {
					greater++;
					if (m[i] < mingtguess) {
						mingtguess = m[i] ;
					}
				} else {
					equal++;
				}
			}
			if (less <= (m.length + 1)/2 && greater <= (m.length + 1)/2) {
				break ;
			} else if (less > greater) {
				max = maxltguess ;
			} else {
				min = mingtguess;
			}
		}
		
		if (less >= (m.length + 1)/2) {
			return maxltguess;
		} else if (less+equal >= (m.length + 1)/2) {
			return guess;
		} else {
			return mingtguess;
		}
	}
	
	public static void main(String[] args) {
		double[] array = new double[1001];
		for (int i = 0; i < array.length; ++i) {
			array[i] = i;
		}
		
		System.out.println(torben(array));
	}

}











